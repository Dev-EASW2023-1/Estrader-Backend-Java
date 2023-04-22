package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RepresentativeListDto {
    private List<RepresentativeDto> representativeDto;

    @Builder
    public RepresentativeListDto(List<RepresentativeDto> representativeDto) {
        this.representativeDto = representativeDto;
    }
}
