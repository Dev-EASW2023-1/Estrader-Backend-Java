package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.dto.user.*;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import com.example.demo.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public String addUserInfo(
            @RequestParam String userid,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String residentId,
            @RequestParam String phoneNum,
            @RequestParam String address,
            @RequestParam String corporateRegistrationNumber,
            @RequestParam String fcmToken,
            @RequestParam String region
    ) {
        UserService.addUserInfo(userid, password, name, residentId, phoneNum, address, corporateRegistrationNumber, fcmToken, region);

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
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.findUserInfo());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDataResponse> registerMember(
            @RequestBody RegisterDataRequest registerDataRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.registerUserInfo(registerDataRequest));
    }

    @PostMapping("/account-exists")
    public ResponseEntity<SignupCheckResponse> checkMember(
            @RequestBody SignupCheckRequest signupCheckRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.checkDuplicateUserInfo(signupCheckRequest));
    }

    @PatchMapping("/login")
    public ResponseEntity<SignInResponse> loginMember(
            @RequestBody SignInRequest signInRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.loginUser(signInRequest));
    }

    @PostMapping("/fcm")
    public ResponseEntity<FcmResponse> pushMember(
            @RequestBody FcmRequest fcmRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.sendByToken(fcmRequest));
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}
