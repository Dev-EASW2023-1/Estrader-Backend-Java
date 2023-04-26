package com.example.demo.app.domain.model.dto.Realtor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RealtorSignInResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @Builder
    public RealtorSignInResponse(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
