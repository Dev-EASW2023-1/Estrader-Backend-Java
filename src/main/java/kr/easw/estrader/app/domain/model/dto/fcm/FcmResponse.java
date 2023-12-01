package kr.easw.estrader.app.domain.model.dto.fcm;

import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmResponse {
    @NotBlank
    private Boolean isSuccess;

    @NotBlank
    private String message;

    @Builder
    public FcmResponse(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public FcmResponse(ErrorCode code) {
        this.isSuccess = code.getIsSuccess();
        this.message = code.getMessage();
    }

    public static FcmResponse of(ErrorCode code) {
        return new FcmResponse(code);
    }
}
