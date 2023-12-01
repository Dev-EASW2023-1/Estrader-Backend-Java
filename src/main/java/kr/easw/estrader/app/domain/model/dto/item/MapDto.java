package kr.easw.estrader.app.domain.model.dto.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MapDto {
    @NotBlank
    private String district;

    public static MapDto withDistrict(String district) {
        MapDto mapDto = new MapDto();
        mapDto.district = district;
        return mapDto;
    }
}
