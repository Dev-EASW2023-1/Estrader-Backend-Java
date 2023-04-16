package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.*;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void addUserinfo(
            String userid,
            String password,
            String residentid,
            String phonenum,
            String address
    ) {
        UserEntity userinfo = UserEntity.builder()
                .userid(userid)
                .password(password)
                .residentid(residentid)
                .phonenum(phonenum)
                .address(address)
                .build();
        userRepository.save(userinfo);
    }

    public void printAllUserIds() {
        List<UserEntity> userList = userRepository.findAll();
        for (UserEntity user : userList) {
            System.out.println("User ID: " + user.getUserid());
        }
    }

    public UserListDto findUserinfo() {
        List<UserEntity> listUserinfo = userRepository.findAll();

        if(listUserinfo == null){
            throw new IllegalArgumentException();
        }

        List<UserDto> listUserDto = listUserinfo.stream()
                .map(m -> new UserDto(m.getUserid(), m.getPassword(), m.getAddress(), m.getPhonenum(), m.getResidentid()))
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

        System.out.println("널 값이 맞습니까?" + isUserExists.isPresent());

        if(isUserExists.isPresent()){
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

    public SignupCheckResponse CheckDuplicateUserinfo(SignupCheckRequest signupCheckRequest) {
        Optional<UserEntity> isUserExists =
                userRepository.findByUserid(signupCheckRequest.getUserid());

        System.out.println("널 값이 맞습니까?" + isUserExists.isPresent());

        if(isUserExists.isPresent()){
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
}
