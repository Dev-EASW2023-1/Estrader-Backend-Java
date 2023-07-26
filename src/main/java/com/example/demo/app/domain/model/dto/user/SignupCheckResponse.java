package com.example.demo.app.domain.model.dto.user;

import com.example.demo.app.domain.model.dto.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupCheckResponse {
    @NotBlank
    private Boolean isDuplicated;

    @NotBlank
    private String message;

    @Builder
    public SignupCheckResponse(Boolean isDuplicated, String message) {
        this.isDuplicated = isDuplicated;
        this.message = message;
    }

    public SignupCheckResponse(ErrorCode code) {
        this.isDuplicated = code.getIsSuccess();
        this.message = code.getMessage();
    }

    public static SignupCheckResponse of(ErrorCode code) {
        return new SignupCheckResponse(code);
    }
}
