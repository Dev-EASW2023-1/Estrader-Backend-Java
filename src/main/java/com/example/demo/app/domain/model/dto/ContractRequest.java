package com.example.demo.app.domain.model.dto;

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
    private String representativeId;

    @NotBlank
    private String itemId;

    @Builder
    public ContractRequest(String userId, String representativeId, String itemId) {
        this.userId = userId;
        this.representativeId = representativeId;
        this.itemId = itemId;
    }
}
