package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.UserDto;
import com.example.demo.app.domain.model.dto.UserListDto;
import com.example.demo.app.domain.model.entity.User;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void addUserinfo(
            String userid,
            String password,
            String address,
            String phonenum,
            String residentid

    ) {
        User userinfo = User.builder()
                .userid(userid)
                .password(password)
                .address(address)
                .phonenum(phonenum)
                .residentid(residentid)
                .build();
        userRepository.save(userinfo);

    }
    public void printAllUserIds() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            System.out.println("User ID: " + user.getUserid());
        }
    }

    public UserListDto findUserinfo() {
        List<User> listUserinfo = userRepository.findAll();

        if(listUserinfo == null){
            throw new IllegalArgumentException();
        }

        List<UserDto> listUserDto = listUserinfo.stream()
                .map(m -> new UserDto(m.getUserid(), m.getPassword(), m.getAddress(), m.getPhonenum(), m.getResidentid()))
                .collect(Collectors.toList());

        UserListDto.builder()
                .userDto(listUserDto)
                .build();

        return UserListDto.builder().userDto(listUserDto).build();
    }


}
