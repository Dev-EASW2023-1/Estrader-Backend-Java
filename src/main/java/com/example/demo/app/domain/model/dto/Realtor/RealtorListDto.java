package com.example.demo.app.domain.model.dto.Realtor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RealtorListDto {
    private List<RealtorDto> realtorDto;

    @Builder
    public RealtorListDto(List<RealtorDto> realtorDto) {
        this.realtorDto = realtorDto;
    }
}
