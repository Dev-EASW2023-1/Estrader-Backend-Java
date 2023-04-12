package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemListDto {

    @NotBlank
    private List<ItemDto> itemDto;

    @Builder
    public ItemListDto(List<ItemDto> itemDto) {
        this.itemDto = itemDto;
    }
}
