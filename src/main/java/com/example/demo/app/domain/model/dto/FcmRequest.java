package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class FcmRequest {
    @NotBlank
    private String userid;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @Builder
    public FcmRequest(String userid, String title, String body) {
        this.userid = userid;
        this.title = title;
        this.body = body;
    }
}