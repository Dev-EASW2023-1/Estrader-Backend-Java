package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/add")
    public String addUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String day
    ) {
        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .day(day)
                .build();
        userRepository.save(user);

        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String showUserList(
            Model model
    ) {
        List<UserEntity> userList = userRepository.findAll();
        userList.forEach(userEntity ->
                        model.addAttribute("user", userEntity)
        );

        return "user/list";
    }
}

