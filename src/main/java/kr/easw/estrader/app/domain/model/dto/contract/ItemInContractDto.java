package kr.easw.estrader.app.domain.model.dto.contract;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemInContractDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String realtorId;

    @NotBlank
    private String itemId;

    @Builder
    public ItemInContractDto(String userId, String realtorId, String itemId) {
        this.userId = userId;
        this.realtorId = realtorId;
        this.itemId = itemId;
    }
}
