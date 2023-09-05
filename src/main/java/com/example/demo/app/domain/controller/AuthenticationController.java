package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.auth.JwtAuthenticateFilter;
import com.example.demo.app.domain.auth.JwtToken;
import com.example.demo.app.domain.model.dto.Realtor.*;
import com.example.demo.app.domain.model.dto.user.*;
import com.example.demo.app.domain.service.AuthenticationService;
import com.example.demo.app.domain.service.RealtorService;
import com.example.demo.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService UserService;
    private final RealtorService realtorService;
    private final AuthenticationService authenticationService;

    @PatchMapping("/user/login")
    public ResponseEntity<SignInResponse> loginMember(
            @RequestBody SignInRequest signInRequest
    ) {
        JwtToken token = authenticationService.authenticate(signInRequest.getUserId(), signInRequest.getPassword());
        return ResponseEntity.ok()
                .headers(getAuthTokenToHeaders(token))
                .body(UserService.loginUser(signInRequest, token));
    }

    @PostMapping("/user/register")
    public ResponseEntity<RegisterDataResponse> registerMember(
            @RequestBody RegisterDataRequest registerDataRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.registerUserInfo(registerDataRequest));
    }

    @PostMapping("/user/account-exists")
    public ResponseEntity<SignupCheckResponse> checkMember(
            @RequestBody SignupCheckRequest signupCheckRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(UserService.checkDuplicateUserInfo(signupCheckRequest));
    }

    @PatchMapping("/realtor/login")
    public ResponseEntity<RealtorSignInResponse> loginMember(
            @RequestBody RealtorSignInRequest realtorSignInRequest
    ) {
        JwtToken token = authenticationService.authenticate(realtorSignInRequest.getRealtorId(), realtorSignInRequest.getPassword());
        return ResponseEntity.ok()
                .headers(getAuthTokenToHeaders(token))
                .body(realtorService.loginRealtor(realtorSignInRequest, token));
    }

    @PostMapping("/realtor/register")
    public ResponseEntity<RealtorRegisterDataResponse> registerMember(
            @RequestBody RealtorRegisterDataRequest realtorRegisterDataRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(realtorService.registerRealtorInfo(realtorRegisterDataRequest));
    }

    @PostMapping("/realtor/account-exists")
    public ResponseEntity<RealtorSignupCheckResponse> checkMember(
            @RequestBody RealtorSignupCheckRequest realtorSignupCheckRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(realtorService.checkDuplicateRealtorInfo(realtorSignupCheckRequest));
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }

    private HttpHeaders getAuthTokenToHeaders(JwtToken token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.add(JwtAuthenticateFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());
        return headers;
    }
}
