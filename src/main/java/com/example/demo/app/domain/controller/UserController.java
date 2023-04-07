package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.UserDto;
import com.example.demo.app.domain.model.dto.UserListDto;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import com.example.demo.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/add")
    public String addUserEntity(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String day
    ) {
        userService.addUser(username,password,day);
        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String showUserList(
            Model model
    ) {
        List<UserEntity> userList = userRepository.findAll();
        model.addAttribute("userList", userList);

        return "user/list";
    }

    @GetMapping("/test")
    public ResponseEntity<UserListDto> Test() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(userService.findUser(), headers, HttpStatus.OK);
    }
}

