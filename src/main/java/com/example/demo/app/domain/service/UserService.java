package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.UserDto;
import com.example.demo.app.domain.model.dto.UserListDto;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void addUser(
            String username,
            String password,
            String day
    ) {
        UserEntity user = UserEntity.builder()
                .picture(username)
                .information(password)
                .period(day)
                .build();
        userRepository.save(user);
    }

    public UserListDto findUser() {
        List<UserEntity> listUser = userRepository.findAll();

        if(listUser == null){
            throw new IllegalArgumentException();
        }

        List<UserDto> listUserDto = listUser.stream()
                .map(m -> new UserDto(m.getPicture(), m.getInformation(), m.getPeriod(), m.getLocation(), m.getReserveprice(),m.getAuctionperiod()))
                .collect(Collectors.toList());

        UserListDto.builder()
                .userDto(listUserDto)
                .build();

        return UserListDto.builder().userDto(listUserDto).build();
    }
}
