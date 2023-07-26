package com.example.demo.app.domain.model.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemListDto {
    private List<ItemDto> itemDto;

    @Builder
    public ItemListDto(List<ItemDto> itemDto) {
        this.itemDto = itemDto;
    }
}
