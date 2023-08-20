package com.example.demo.app.domain.service;

import com.example.demo.app.domain.exception.exceptions.FcmFailureException;
import com.example.demo.app.domain.exception.exceptions.LoginFailureException;
import com.example.demo.app.domain.exception.exceptions.NotFoundException;
import com.example.demo.app.domain.model.dto.Realtor.RealtorDto;
import com.example.demo.app.domain.model.dto.Realtor.RealtorListDto;
import com.example.demo.app.domain.model.dto.Realtor.RealtorSignInRequest;
import com.example.demo.app.domain.model.dto.Realtor.RealtorSignInResponse;
import com.example.demo.app.domain.model.dto.error.ErrorCode;
import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.entity.RealtorEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RealtorRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealtorService {

    private final PasswordEncoder passwordEncoder;
    private final RealtorRepository realtorRepository;
    private final UserRepository userRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;

    @Transactional
    public void addRealtorInfo(
            String realtorId,
            String password,
            String name,
            String residentNumber,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken,
            String region
    ) {
        RealtorEntity representativeInfo = RealtorEntity.builder()
                .realtorId(realtorId)
                .password(passwordEncoder.encode(password)) // password should be encoded
                .name(name)
                .residentNumber(residentNumber)
                .phoneNumber(phoneNumber)
                .address(address)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .fcmToken(fcmToken)
                .region(region)
                .build();
        realtorRepository.save(representativeInfo);
    }

    public RealtorListDto findRealtorInfo() {
        List<RealtorEntity> listRealtorInfo = realtorRepository.findAll();

        List<RealtorDto> listRealtorDto = listRealtorInfo.stream()
                .map(m -> new RealtorDto(
                        m.getRealtorId(),
                        m.getPassword(),
                        m.getName(),
                        m.getResidentNumber(),
                        m.getPhoneNumber(),
                        m.getAddress(),
                        m.getCorporateRegistrationNumber(),
                        m.getFcmToken(),
                        m.getRegion()
                ))
                .collect(Collectors.toList());

        return RealtorListDto.builder()
                .realtorDto(listRealtorDto)
                .build();
    }

    @Transactional
    public RealtorSignInResponse loginRealtor(RealtorSignInRequest realtorSignInRequest) {
        RealtorEntity isRealtorExists =
                realtorRepository.findByRealtorId(realtorSignInRequest.getRealtorId())
                        .orElseThrow(() -> new LoginFailureException(ErrorCode.LOGIN_FAILURE));

        if (!passwordEncoder.matches(realtorSignInRequest.getPassword(),
                isRealtorExists.getPassword())) {
            throw new LoginFailureException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (realtorSignInRequest.getFcmToken() != null) {
            realtorRepository.save(RealtorEntity.of(
                    isRealtorExists,
                    realtorSignInRequest.getFcmToken()
            ));
        }

        log.info("로그인 성공 (대리인) = {}", realtorSignInRequest.getRealtorId());
        log.info("대리인 토큰 = {}", realtorSignInRequest.getFcmToken());

        return RealtorSignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        UserEntity isUserExists = userRepository.findByUserId(fcmRequest.getTargetId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
        itemRepository.findByPhoto(fcmRequest.getItemImage())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        send(isUserExists.getFcmToken(), fcmRequest);

        log.info("전송 from (대리인) {} to (구매자) {}", fcmRequest.getUserId(), fcmRequest.getTargetId());

        return FcmResponse.builder()
                .message("전송에 성공하였습니다.")
                .isSuccess(true)
                .build();
    }

    private void send(String targetToken, FcmRequest fcmRequest) {
        try {
            firebaseCloudMessageService.sendMessageTo(
                    targetToken,
                    fcmRequest
            );
        } catch (IOException e) {
            e.printStackTrace();
            log.error("전송 실패");
            throw new FcmFailureException(ErrorCode.FCM_FAILURE);
        }
    }
}
