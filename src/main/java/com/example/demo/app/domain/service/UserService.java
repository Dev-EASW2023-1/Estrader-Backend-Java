package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.*;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.model.entity.RepresentativeEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RepresentativeRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RepresentativeRepository representativeRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ItemRepository itemRepository;

    @Transactional
    public void addUserinfo(
            String userid,
            String password,
            String residentid,
            String phonenum,
            String address,
            String fcmToken
    ) {
        UserEntity userinfo = UserEntity.builder()
                .userid(userid)
                .password(password)
                .residentid(residentid)
                .phonenum(phonenum)
                .address(address)
                .fcmToken(fcmToken)
                .build();
        userRepository.save(userinfo);
    }

    public UserListDto findUserinfo() {
        List<UserEntity> listUserinfo = userRepository.findAll();

        List<UserDto> listUserDto = listUserinfo.stream()
                .map(m -> new UserDto(
                        m.getUserid(),
                        m.getPassword(),
                        m.getAddress(),
                        m.getPhonenum(),
                        m.getResidentid(),
                        m.getFcmToken()
                ))
                .collect(Collectors.toList());

        return UserListDto.builder()
                .userDto(listUserDto)
                .build();
    }

    @Transactional
    public RegisterDataResponse registerUserinfo(RegisterDataRequest registerDataRequest) {
        System.out.println(registerDataRequest.getUserid());

        Optional<UserEntity> isUserExists =
                userRepository.findByUserid(registerDataRequest.getUserid());

        if (isUserExists.isPresent()) {
            return RegisterDataResponse.builder()
                    .isSuccess(false)
                    .message("회원가입에 실패하였습니다.")
                    .build();
        }

        userRepository.save(registerDataRequest.toEntity());

        return RegisterDataResponse.builder()
                .isSuccess(true)
                .message("회원가입에 성공하였습니다.")
                .build();
    }

    public SignupCheckResponse checkDuplicateUserinfo(SignupCheckRequest signupCheckRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserid(signupCheckRequest.getUserid());

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
                userRepository.findByUserid(signInRequest.getUserid());

        if (isUserExists.isEmpty()) {
            return SignInResponse.builder()
                    .isSuccess(false)
                    .message("로그인에 실패했습니다.")
                    .build();
        }

        if (!isUserExists.get().getPassword()
                .equals(signInRequest.getPassword())) {
            return SignInResponse.builder()
                    .isSuccess(false)
                    .message("비밀번호가 다릅니다.")
                    .build();
        }

        if (signInRequest.getFcmToken() != null) {
            userRepository.save(
                    UserEntity.builder()
                            .id(isUserExists.get().getId())
                            .userid(isUserExists.get().getUserid())
                            .password(isUserExists.get().getPassword())
                            .residentid(isUserExists.get().getResidentid())
                            .phonenum(isUserExists.get().getPhonenum())
                            .address(isUserExists.get().getAddress())
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
        RepresentativeEntity isRepresentativeExists = representativeRepository.findByUsername(fcmRequest.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("representative doesn't exist"));

        itemRepository.findByPicture(fcmRequest.getItemImage())
                .orElseThrow(() -> new IllegalArgumentException("item doesn't exist"));

        try {
            firebaseCloudMessageService.sendMessageTo(
                    isRepresentativeExists.getFcmToken(),
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
