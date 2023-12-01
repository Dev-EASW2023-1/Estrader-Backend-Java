package kr.easw.estrader.app.domain.model.dto.Realtor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RealtorSignupCheckRequest {
    @NotBlank
    private String realtorId;

    @Builder
    public RealtorSignupCheckRequest(String realtorId) {
        this.realtorId = realtorId;
    }
}
