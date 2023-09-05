package com.example.demo.app.domain.model.dto.Realtor;

import com.example.demo.app.domain.model.dto.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RealtorRegisterDataResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @Builder
    public RealtorRegisterDataResponse(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public RealtorRegisterDataResponse(ErrorCode code) {
        this.isSuccess = code.getIsSuccess();
        this.message = code.getMessage();
    }

    public static RealtorRegisterDataResponse of(ErrorCode code) {
        return new RealtorRegisterDataResponse(code);
    }
}
