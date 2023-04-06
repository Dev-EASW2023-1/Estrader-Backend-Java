package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.User;
import com.example.demo.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String day) {
        User user = User.builder().
                username(username).
                password(password).
                day(day).
                build();
        userRepository.save(user);

        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String userList(Model model) {
        List<User> userList = (List<User>) userRepository.findAll();
        model.addAttribute("userList", userList);

        return "user/list";
    }

}

