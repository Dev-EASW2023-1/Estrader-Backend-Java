package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RepresentativeDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fcmToken;

    @NotBlank
    private String corporateRegistrationNumber;

    @Builder
    public RepresentativeDto(String username, String password, String fcmToken, String corporateRegistrationNumber) {
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
    }
}
