package com.example.demo.app.domain.model.dto.contract;

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

    @NotBlank
    private String name;

    @Builder
    public ContractResponse(Boolean isSuccess, String message, String name) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.name = name;
    }
}
