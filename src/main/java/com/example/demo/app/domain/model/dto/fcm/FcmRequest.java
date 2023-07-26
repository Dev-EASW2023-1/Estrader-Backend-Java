package com.example.demo.app.domain.model.dto.fcm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String targetId;

    @NotBlank
    private String itemImage;

    @NotBlank
    private String phase;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @Builder
    public FcmRequest(String userId, String targetId, String itemImage, String phase, String title, String body) {
        this.userId = userId;
        this.targetId = targetId;
        this.itemImage = itemImage;
        this.phase = phase;
        this.title = title;
        this.body = body;
    }
}