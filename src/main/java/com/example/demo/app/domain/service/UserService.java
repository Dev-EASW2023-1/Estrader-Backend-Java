package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.dto.user.*;
import com.example.demo.app.domain.model.entity.RealtorEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RealtorRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
            String fcmToken
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
                        m.getFcmToken()
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
            return RegisterDataResponse.builder()
                    .isSuccess(false)
                    .message("회원가입에 실패하였습니다.")
                    .build();
        }

        String encodedPassword = passwordEncoder.encode(registerDataRequest.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .userId(registerDataRequest.getUserId())
                .password(encodedPassword)
                .name(registerDataRequest.getName())
                .residentNumber(registerDataRequest.getResidentNumber())
                .phoneNumber(registerDataRequest.getPhoneNumber())
                .address(registerDataRequest.getAddress())
                .corporateRegistrationNumber(registerDataRequest.getCorporateRegistrationNumber())
                .fcmToken(registerDataRequest.getFcmToken())
                .build();

        userRepository.save(userEntity);

        return RegisterDataResponse.builder()
                .isSuccess(true)
                .message("회원가입에 성공하였습니다.")
                .build();
    }

    public SignupCheckResponse checkDuplicateUserInfo(SignupCheckRequest signupCheckRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(signupCheckRequest.getUserId());

        if (isUserExists.isPresent()) {
            return SignupCheckResponse.builder()
                    .isDuplicated(false)
                    .message("아이디가 이미 존재합니다.")
                    .build();
        }

        return SignupCheckResponse.builder()
                .isDuplicated(true)
                .message("사용할 수 있는 아이디입니다.")
                .build();
    }

    @Transactional
    public SignInResponse loginUser(SignInRequest signInRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(signInRequest.getUserId());

        if (isUserExists.isEmpty()) {
            return SignInResponse.builder()
                    .isSuccess(false)
                    .message("로그인에 실패했습니다.")
                    .build();
        }

        if (!passwordEncoder.matches(signInRequest.getPassword(), isUserExists.get().getPassword())) {
            return SignInResponse.builder()
                    .isSuccess(false)
                    .message("비밀번호가 다릅니다.")
                    .build();
        }

        if (signInRequest.getFcmToken() != null) {
            userRepository.save(
                    UserEntity.login()
                            .id(isUserExists.get().getId())
                            .userId(isUserExists.get().getUserId())
                            .password(isUserExists.get().getPassword())
                            .name(isUserExists.get().getName())
                            .residentNumber(isUserExists.get().getResidentNumber())
                            .phoneNumber(isUserExists.get().getPhoneNumber())
                            .address(isUserExists.get().getAddress())
                            .corporateRegistrationNumber(isUserExists.get().getCorporateRegistrationNumber())
                            .fcmToken(signInRequest.getFcmToken())
                            .build()
            );
        }

        return SignInResponse.builder()
                .isSuccess(true)
                .message("로그인에 성공했습니다.")
                .build();
    }

    public FcmResponse sendByToken(FcmRequest fcmRequest) {
        RealtorEntity isRealtorExists = realtorRepository.findByRealtorId(fcmRequest.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("representative doesn't exist"));

        itemRepository.findByPhoto(fcmRequest.getItemImage())
                .orElseThrow(() -> new IllegalArgumentException("item doesn't exist"));

        try {
            firebaseCloudMessageService.sendMessageTo(
                    isRealtorExists.getFcmToken(),
                    fcmRequest.getTitle(),
                    fcmRequest.getBody(),
                    fcmRequest.getUserId(),
                    fcmRequest.getTargetId(),
                    fcmRequest.getItemImage(),
                    fcmRequest.getPhase()
            );
        } catch (IOException e) {
            e.printStackTrace();
            return FcmResponse.builder()
                    .message("전송에 실패하였습니다.")
                    .isSuccess(false)
                    .build();
        }
        return FcmResponse.builder()
                .message("전송에 성공하였습니다.")
                .isSuccess(true)
                .build();
    }
}
