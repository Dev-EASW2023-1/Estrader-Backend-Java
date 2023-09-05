package com.example.demo.app.domain.model.dto.Realtor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RealtorRegisterDataRequest {
    @NotBlank
    private String realtorId;

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
    public RealtorRegisterDataRequest(String realtorId, String password, String name, String residentNumber, String phoneNumber, String address, String corporateRegistrationNumber, String fcmToken, String region) {
        this.realtorId = realtorId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
        this.region = region;
    }
}
