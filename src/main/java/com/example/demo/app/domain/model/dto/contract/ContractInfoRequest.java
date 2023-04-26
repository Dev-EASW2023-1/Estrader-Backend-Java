package com.example.demo.app.domain.model.dto.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ContractInfoRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String realtorId;

    @NotBlank
    private String itemId;

    @Builder
    public ContractInfoRequest(String userId, String realtorId, String itemId) {
        this.userId = userId;
        this.realtorId = realtorId;
        this.itemId = itemId;
    }
}
