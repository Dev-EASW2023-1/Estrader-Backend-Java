package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ContractResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @Builder
    public ContractResponse(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
