package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void addUser() {
        UserEntity user = UserEntity.builder().
                username("<img src=\"https://example.com/images/item.jpg\" alt=\"아이템\">").
                password("비번").
                day("123").
                build();
        System.out.println(user);

        userRepository.save(user);
    }

}
