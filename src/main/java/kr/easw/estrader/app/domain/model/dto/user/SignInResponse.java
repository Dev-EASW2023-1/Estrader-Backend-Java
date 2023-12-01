package kr.easw.estrader.app.domain.model.dto.user;

import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @NotBlank
    private String token;

    @Builder
    public SignInResponse(Boolean isSuccess, String message, String token) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.token = token;
    }

    public SignInResponse(ErrorCode code) {
        this.isSuccess = code.getIsSuccess();
        this.message = code.getMessage();
        this.token = "";
    }

    public static SignInResponse of(ErrorCode code) {
        return new SignInResponse(code);
    }
}
