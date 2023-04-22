package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RepresentativeSignInRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fcmToken;

    @Builder
    public RepresentativeSignInRequest(String username, String password, String fcmToken) {
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}
