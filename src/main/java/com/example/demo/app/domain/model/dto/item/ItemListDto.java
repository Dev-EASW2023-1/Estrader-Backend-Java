package com.example.demo.app.domain.model.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemListDto {
    private List<ItemDto> itemDto;

    @Builder
    public ItemListDto(List<ItemDto> itemDto) {
        this.itemDto = itemDto;
    }
}
