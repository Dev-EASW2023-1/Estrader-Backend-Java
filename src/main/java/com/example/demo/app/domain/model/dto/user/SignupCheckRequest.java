package com.example.demo.app.domain.model.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignupCheckRequest {
    @NotBlank
    private String userId;

    @Builder
    public SignupCheckRequest(String userId) {
        this.userId = userId;
    }
}
