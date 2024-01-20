package kr.easw.estrader.app.domain.model.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPageRequestDTO {
    @NotBlank
    private String district;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    @Builder
    public ItemPageRequestDTO(String district, Integer page, Integer size) {
        this.district = district;
        this.page = page;
        this.size = size;
    }
}