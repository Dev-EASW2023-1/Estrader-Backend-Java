package com.example.demo.app.domain.model.dto.user;

import com.example.demo.app.domain.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RegisterDataRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String residentNumber;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String corporateRegistrationNumber;

    @NotBlank
    private String fcmToken;

    @NotBlank
    private String region;

    @Builder
    public RegisterDataRequest(String userId, String password, String name, String residentNumber, String phoneNumber, String address, String corporateRegistrationNumber, String fcmToken, String region) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
        this.region = region;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .residentNumber(residentNumber)
                .phoneNumber(phoneNumber)
                .address(address)
                .corporateRegistrationNumber(corporateRegistrationNumber)
                .fcmToken(fcmToken)
                .region(region)
                .build();
    }
}
