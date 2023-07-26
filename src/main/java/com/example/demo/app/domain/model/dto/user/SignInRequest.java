package com.example.demo.app.domain.model.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String fcmToken;

    @Builder
    public SignInRequest(String userId, String password, String fcmToken) {
        this.userId = userId;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}