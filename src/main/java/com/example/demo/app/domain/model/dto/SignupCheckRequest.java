package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignupCheckRequest {
    @NotBlank
    private String userid;

    @Builder
    public SignupCheckRequest(String userid) {
        this.userid = userid;
    }
}
