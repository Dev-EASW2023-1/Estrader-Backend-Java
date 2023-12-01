package kr.easw.estrader.app.domain.model.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupCheckRequest {
    @NotBlank
    private String userId;

    @Builder
    public SignupCheckRequest(String userId) {
        this.userId = userId;
    }
}
