package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.contract.ContractRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmMessageDto;
import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {
    private final ObjectMapper objectMapper;

    private final OkHttpClient okHttpClient;

    @Value("${fcm.project.name}")
    private String fcmProjectName;

    @Value("${fcm.config.path}")
    private String firebaseConfigPath;

    public void sendMessageTo(
            String targetToken,
            String title,
            String body,
            String userId,
            String targetId,
            String itemImage,
            String phase
    ) throws IOException {
        String message = makeMessage(
                targetToken,
                title,
                body,
                userId,
                targetId,
                itemImage,
                phase
        );

        handleFcmMessageWithOkHttp(message);
    }

    public void sendMessageTo(
            String targetToken,
            FcmRequest fcmRequest
    ) throws IOException {
        String message = makeMessage(
                targetToken,
                fcmRequest.getTitle(),
                fcmRequest.getBody(),
                fcmRequest.getUserId(),
                fcmRequest.getTargetId(),
                fcmRequest.getItemImage(),
                fcmRequest.getPhase()
        );

        handleFcmMessageWithOkHttp(message);
    }

    public void sendMessageTo(
            String targetToken,
            ContractRequest contractRequest
    ) throws IOException {
        String message = makeMessage(
                targetToken,
                contractRequest.getTitle(),
                contractRequest.getBody(),
                contractRequest.getUserId(),
                contractRequest.getRealtorId(),
                contractRequest.getItemId(),
                contractRequest.getPhase()
        );

        handleFcmMessageWithOkHttp(message);
    }

    // HTTP 통신을 간편하게 사용할 수 있도록 만들어진 자바 라이브러리 OkHttp
    public void handleFcmMessageWithOkHttp(
            String message
    ) throws IOException {

        // RequestBody 생성
        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                message
        );

        // Post 요청 객체 생성
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/" + fcmProjectName + "/messages:send")
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        // 요청 전송
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.info("fcm 전송 성공 responseCode = {}, errorMessage = {}", response.code(), response.message());
            } else {
                log.warn("fcm 전송 실패 responseCode = {}, errorMessage = {}", response.code(), response.message());
            }
        } catch (IOException e) {
            log.error("fcm 전송 실패");
        }
    }

    // Java Object 를 JSON 형식의 문자열로 변환
    private String makeMessage(
            String targetToken,
            String title,
            String body,
            String userId,
            String targetId,
            String itemImage,
            String phase
    ) throws JsonProcessingException {

        FcmMessageDto fcmMessage =
                FcmMessageDto.builder()
                .message(
                        FcmMessageDto.Message.builder()
                        .token(targetToken)
                        .notification(
                                FcmMessageDto.Notification.builder()
                                    .title(title)
                                    .body(body)
                                    .build()
                        )
                        .data(
                                FcmMessageDto.Data.builder()
                                    .userId(userId)
                                    .targetId(targetId)
                                    .itemImage(itemImage)
                                    .phase(phase)
                                    .build()
                        )
                        .android(
                                FcmMessageDto.Android.builder()
                                .priority("high")
                                .build()
                        )
                        .build()
                )
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    // Google API 클라이언트 라이브러리를 사용하여 비공개 키 JSON 파일 참조하여 토큰 발급
    // 토큰이 만료되면 토큰 새로고침 메소드가 자동으로 호출되어 업데이트된 토큰 발급
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(
                        List.of("https://www.googleapis.com/auth/firebase",
                                "https://www.googleapis.com/auth/cloud-platform",
                                "https://www.googleapis.com/auth/firebase.readonly"));
        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }
}
