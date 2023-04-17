package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignInRequest {
    @NotBlank
    private String userid;

    @NotBlank
    private String password;

    @NotBlank
    private String fcmToken;

    @Builder
    public SignInRequest(String userid, String password, String fcmToken) {
        this.userid = userid;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}