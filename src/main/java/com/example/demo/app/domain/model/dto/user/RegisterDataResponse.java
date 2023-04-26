package com.example.demo.app.domain.model.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RegisterDataResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @Builder
    public RegisterDataResponse(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
