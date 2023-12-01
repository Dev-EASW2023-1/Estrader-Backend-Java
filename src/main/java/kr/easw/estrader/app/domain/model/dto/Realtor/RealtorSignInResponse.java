package kr.easw.estrader.app.domain.model.dto.Realtor;

import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RealtorSignInResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @NotBlank
    private String token;

    @Builder
    public RealtorSignInResponse(Boolean isSuccess, String message, String token) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.token = token;
    }

    public RealtorSignInResponse(ErrorCode code) {
        this.isSuccess = code.getIsSuccess();
        this.message = code.getMessage();
        this.token = "";
    }

    public static RealtorSignInResponse of(ErrorCode code) {
        return new RealtorSignInResponse(code);
    }
}
