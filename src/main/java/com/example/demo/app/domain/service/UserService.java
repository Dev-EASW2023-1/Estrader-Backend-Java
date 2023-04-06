package com.example.demo.app.domain.service;

import com.example.demo.app.domain.User;
import com.example.demo.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void addUser() {
        User user = User.builder().
                username("<img src=\"https://example.com/images/item.jpg\" alt=\"아이템\">").
                password("비번").
                day("123").
                build();
        System.out.println(user);

        userRepository.save(user);
    }

}
