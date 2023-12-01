package kr.easw.estrader.app.domain.model.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LookUpItemRequest {
    @NotBlank
    private String photo;

    @Builder
    public LookUpItemRequest(String photo){
        this.photo = photo;
    }
}
