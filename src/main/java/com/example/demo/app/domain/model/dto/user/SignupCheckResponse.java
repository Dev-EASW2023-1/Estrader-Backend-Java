package com.example.demo.app.domain.model.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignupCheckResponse {
    @NotBlank
    private Boolean isDuplicated;

    @NotBlank
    private String message;

    @Builder
    public SignupCheckResponse(Boolean isDuplicated, String message) {
        this.isDuplicated = isDuplicated;
        this.message = message;
    }
}
