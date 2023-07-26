package com.example.demo.app.domain.service;

import com.example.demo.app.domain.exception.exceptions.*;
import com.example.demo.app.domain.model.dto.error.ErrorCode;
import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.dto.user.*;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RealtorRepository realtorRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;

    @Transactional
    public void addUserInfo(
            String userid,
            String password,
            String name,
            String residentId,
            String phoneNum,
            String address,
            String corporateRegistrationNumber,
            String fcmToken,
            String region
    ) {
        UserEntity userinfo = UserEntity.builder()
                .userId(userid)
                .password(passwordEncoder.encode(password)) // password should be encoded
                .residentNumber(residentId)
                .name(name)
                .phoneNumber(phoneNum)
                .address(address)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .fcmToken(fcmToken)
                .region(region)
                .build();
        userRepository.save(userinfo);
    }

    public UserListDto findUserInfo() {
        List<UserEntity> listUserinfo = userRepository.findAll();

        List<UserDto> listUserDto = listUserinfo.stream()
                .map(m -> new UserDto(
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

        return UserListDto.builder()
                .userDto(listUserDto)
                .build();
    }

    @Transactional
    public RegisterDataResponse registerUserInfo(RegisterDataRequest registerDataRequest) {
        System.out.println(registerDataRequest.getUserId());

        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(registerDataRequest.getUserId());

        if (isUserExists.isPresent()) {
            log.info("회원가입 실패");
            throw new SignupFailureException(ErrorCode.SIGNUP_FAILURE);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerDataRequest.getPassword());

        userRepository.save(UserEntity.of(
                registerDataRequest,
                encodedPassword
        ));

        log.info("회원가입 성공 = {}", registerDataRequest.getUserId());

        return RegisterDataResponse.builder()
                .isSuccess(true)
                .message("회원가입에 성공하였습니다.")
                .build();
    }

    public SignupCheckResponse checkDuplicateUserInfo(SignupCheckRequest signupCheckRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(signupCheckRequest.getUserId());

        if (isUserExists.isPresent()) {
            throw new DuplicateIdException(ErrorCode.DUPLICATE_ID);
        }

        log.info("사용할 수 있는 아이디입니다. = {}", signupCheckRequest.getUserId());
        return SignupCheckResponse.builder()
                .isDuplicated(true)
                .message("사용할 수 있는 아이디입니다.")
                .build();
    }

    @Transactional
    public SignInResponse loginUser(SignInRequest signInRequest) {
        UserEntity isUserExists =
                userRepository.findByUserId(signInRequest.getUserId())
                        .orElseThrow(() -> new LoginFailureException(ErrorCode.LOGIN_FAILURE));

        if (!passwordEncoder.matches(signInRequest.getPassword(),
                isUserExists.getPassword())) {
            throw new LoginFailureException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (signInRequest.getFcmToken() != null) {
            userRepository.save(UserEntity.of(
                    isUserExists,
                    signInRequest.getFcmToken()
            ));
        }

        log.info("로그인 성공 (구매자) = {}", signInRequest.getUserId());

        return SignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        RealtorEntity isRealtorExists = realtorRepository.findByRealtorId(fcmRequest.getTargetId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
        itemRepository.findByPhoto(fcmRequest.getItemImage())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        send(isRealtorExists.getFcmToken(), fcmRequest);

        log.info("전송 성공 from (구매자) {} to (대리인) {}", fcmRequest.getUserId(), fcmRequest.getTargetId());
        log.info("대리인 토큰 = {}", isRealtorExists.getFcmToken());

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
            log.info("전송 실패");
            throw new FcmFailureException(ErrorCode.FCM_FAILURE);
        }
    }
}
