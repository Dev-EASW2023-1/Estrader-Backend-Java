package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.*;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import com.example.demo.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService UserService;

    // 유저 저장
    @PostMapping("/add")
    public String addUserinfo(
            @RequestParam String userid,
            @RequestParam String password,
            @RequestParam String residentid,
            @RequestParam String phonenum,
            @RequestParam String address,
            @RequestParam String fcmToken
    ) {
        UserService.addUserinfo(userid,password, residentid, phonenum, address, fcmToken);

        return "redirect:/user/list";
    }

    // 유저 리스트 출력
    @GetMapping("/list")
    public String showListUserinfo(
            Model model
    ) {
        List<UserEntity> userinfoList = userRepository.findAll();
        model.addAttribute("userinfoList", userinfoList);

        return "user/list";
    }

    @GetMapping("/show-list")
    public ResponseEntity<UserListDto> showMember() {
        return new ResponseEntity<>(UserService.findUserinfo(), getJsonHeader(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDataResponse> postMember(
            @RequestBody RegisterDataRequest registerDataRequest
    ) {
        return new ResponseEntity<>(UserService.registerUserinfo(registerDataRequest), getJsonHeader(), HttpStatus.OK);
    }

    @PostMapping("/account-exists")
    public ResponseEntity<SignupCheckResponse> checkMember(
            @RequestBody SignupCheckRequest signupCheckRequest
    ) {
        return new ResponseEntity<>(UserService.checkDuplicateUserinfo(signupCheckRequest), getJsonHeader(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> loginMember(
            @RequestBody SignInRequest signInRequest
    ) {
        return new ResponseEntity<>(UserService.loginUser(signInRequest), getJsonHeader(), HttpStatus.OK);
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}
