package com.example.demo.app.domain.model.dto.Realtor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RealtorSignInRequest {
    @NotBlank
    private String realtorId;

    @NotBlank
    private String password;

    @NotBlank
    private String fcmToken;

    @Builder
    public RealtorSignInRequest(
            String realtorId,
            String password,
            String fcmToken
    ) {
        this.realtorId = realtorId;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}
