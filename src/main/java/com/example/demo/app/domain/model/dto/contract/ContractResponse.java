package com.example.demo.app.domain.model.dto.contract;

import com.example.demo.app.domain.model.dto.error.ErrorCode;
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

    public ContractResponse(ErrorCode code) {
        this.isSuccess = code.getIsSuccess();
        this.message = code.getMessage();
        this.name = "";
    }

    public static ContractResponse of(ErrorCode code) {
        return new ContractResponse(code);
    }
}
