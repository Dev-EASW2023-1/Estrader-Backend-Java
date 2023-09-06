package com.example.demo.app.domain.service;

import com.example.demo.app.domain.auth.JwtToken;
import com.example.demo.app.domain.exception.exceptions.*;
import com.example.demo.app.domain.model.dto.Realtor.*;
import com.example.demo.app.domain.model.dto.error.ErrorCode;
import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.app.domain.Enum.Role.ROLE_MANAGER;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealtorService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;
    private final AuthenticationService authenticationService;

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
        UserEntity representativeInfo = UserEntity.realtorLogin()
                .userId(realtorId)
                .password(passwordEncoder.encode(password)) // password should be encoded
                .name(name)
                .residentNumber(residentNumber)
                .phoneNumber(phoneNumber)
                .address(address)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .fcmToken(fcmToken)
                .region(region)
                .build();
        userRepository.save(representativeInfo);
    }

    public RealtorListDto findRealtorInfo() {
        List<UserEntity> listRealtorInfo = userRepository.findByRole(ROLE_MANAGER);

        List<RealtorDto> listRealtorDto = listRealtorInfo.stream()
                .map(m -> new RealtorDto(
                        m.getUserId(),
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
    public RealtorRegisterDataResponse registerRealtorInfo(RealtorRegisterDataRequest realtorRegisterDataRequest) {
        Optional<UserEntity> isRealtorExists =
                userRepository.findByUserId(realtorRegisterDataRequest.getRealtorId());

        if (isRealtorExists.isPresent()) {
            log.info("회원가입 실패");
            throw new RealtorSignupFailureException(ErrorCode.SIGNUP_FAILURE);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(realtorRegisterDataRequest.getPassword());

        userRepository.save(UserEntity.of(
                realtorRegisterDataRequest,
                encodedPassword
        ));

        log.info("회원가입 성공 = {}", realtorRegisterDataRequest.getRealtorId());

        JwtToken token = authenticationService.authenticate(realtorRegisterDataRequest.getRealtorId(), realtorRegisterDataRequest.getPassword());

        return RealtorRegisterDataResponse.builder()
                .isSuccess(true)
                .message("회원가입에 성공하였습니다.")
                .token(token.getAccessToken())
                .build();
    }

    public RealtorSignupCheckResponse checkDuplicateRealtorInfo(RealtorSignupCheckRequest realtorSignupCheckRequest) {
        Optional<UserEntity> isRealtorExists =
                userRepository.findByUserId(realtorSignupCheckRequest.getRealtorId());

        if (isRealtorExists.isPresent()) {
            throw new RealtorDuplicateIdException(ErrorCode.DUPLICATE_ID);
        }

        log.info("사용할 수 있는 아이디입니다. = {}", realtorSignupCheckRequest.getRealtorId());
        return RealtorSignupCheckResponse.builder()
                .isDuplicated(true)
                .message("사용할 수 있는 아이디입니다.")
                .build();
    }

    @Transactional
    public RealtorSignInResponse loginRealtor(RealtorSignInRequest realtorSignInRequest, JwtToken token) {
        UserEntity isRealtorExists =
                userRepository.findByUserId(realtorSignInRequest.getRealtorId())
                        .orElseThrow(() -> new RealtorLoginFailureException(ErrorCode.LOGIN_FAILURE));

        if (isRealtorExists.getRole() != ROLE_MANAGER) {
            throw new RealtorLoginFailureException(ErrorCode.INSUFFICIENT_PERMISSIONS);
        }

        if (realtorSignInRequest.getFcmToken() != null) {
            userRepository.save(UserEntity.of(
                    isRealtorExists,
                    realtorSignInRequest.getFcmToken()
            ));
        }

        log.info("로그인 성공 (대리인) = {}", realtorSignInRequest.getRealtorId());
        log.info("대리인 토큰 = {}", realtorSignInRequest.getFcmToken());

        return RealtorSignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .token(token.getAccessToken())
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        UserEntity isUserExists = userRepository.findByUserId(fcmRequest.getTargetId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
        itemRepository.findByPhoto(fcmRequest.getItemImage())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        send(isUserExists.getFcmToken(), fcmRequest);

        log.info("전송 from (대리인) {} to (구매자) {}", fcmRequest.getUserId(), fcmRequest.getTargetId());
        log.info("사용자 토큰 = {}", isUserExists.getFcmToken());

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
