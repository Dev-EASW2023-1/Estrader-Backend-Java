package kr.easw.estrader.app.domain.model.dto.Realtor;

import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RealtorSignupCheckResponse {
    @NotBlank
    private Boolean isDuplicated;

    @NotBlank
    private String message;

    @Builder
    public RealtorSignupCheckResponse(Boolean isDuplicated, String message) {
        this.isDuplicated = isDuplicated;
        this.message = message;
    }

    public RealtorSignupCheckResponse(ErrorCode code) {
        this.isDuplicated = code.getIsSuccess();
        this.message = code.getMessage();
    }

    public static RealtorSignupCheckResponse of(ErrorCode code) {
        return new RealtorSignupCheckResponse(code);
    }
}
