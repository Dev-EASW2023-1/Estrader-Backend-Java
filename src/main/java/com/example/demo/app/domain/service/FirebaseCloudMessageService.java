package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.FcmMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {
    private final ObjectMapper objectMapper;

    // HTTP 통신을 간편하게 사용할 수 있도록 만들어진 자바 라이브러리 OkHttp
    public void sendMessageTo(
            String targetToken,
            String title,
            String body,
            String userId,
            String targetId,
            String itemImage,
            String phase
    ) throws IOException {
        String message = makeMessage(targetToken, title, body, userId, targetId, itemImage, phase);

        // OkHttp 클라이언트 객체 생성
        OkHttpClient client = new OkHttpClient();

        // RequestBody 생성
        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                message
        );

        // Post 요청 객체 생성
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/fir-test-59d19/messages:send")
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        // 요청 전송
        Response response = client.newCall(request)
                .execute();

        if (response.body() != null) {
            System.out.println(response.body().string());
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
                        .build()
                )
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    // Google API 클라이언트 라이브러리를 사용하여 비공개 키 JSON 파일 참조하여 토큰 발급
    // 토큰이 만료되면 토큰 새로고침 메소드가 자동으로 호출되어 업데이트된 토큰 발급
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/fir-test-59d19-firebase-adminsdk-esiqm-c742c40ec1.json";

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
