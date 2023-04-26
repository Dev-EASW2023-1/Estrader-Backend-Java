package com.example.demo.app.domain.model.dto.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ContractRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String realtorId;

    @NotBlank
    private String itemId;

    @NotBlank
    private String phase;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @Builder
    public ContractRequest(String userId, String realtorId, String itemId, String phase, String title, String body) {
        this.userId = userId;
        this.realtorId = realtorId;
        this.itemId = itemId;
        this.phase = phase;
        this.title = title;
        this.body = body;
    }
}
