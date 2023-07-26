package com.example.demo.app.domain.service;

import com.example.demo.app.domain.exception.exceptions.NotFoundException;
import com.example.demo.app.domain.model.dto.error.ErrorCode;
import com.example.demo.app.domain.model.dto.item.ItemDto;
import com.example.demo.app.domain.model.dto.item.ItemListDto;
import com.example.demo.app.domain.model.dto.item.LookUpItemRequest;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public void addItem(
            String caseNumber,
            String court,
            String location,
            String minimumBidPrice,
            String photo,
            String biddingPeriod,
            String itemType,
            String note,
            String managementNumber

    ) {
        ItemEntity item = ItemEntity.builder()
                .caseNumber(caseNumber)
                .court(court)
                .location(location)
                .minimumBidPrice(minimumBidPrice)
                .photo(photo)
                .biddingPeriod(biddingPeriod)
                .itemType(itemType)
                .note(note)
                .managementNumber(managementNumber)
                .build();
        itemRepository.save(item);
    }

    public ItemListDto findItemList() {
        List<ItemEntity> listUser = itemRepository.findAll();

        List<ItemDto> listItemDto = listUser.stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    public ItemDto lookUpItem(LookUpItemRequest lookUpItemRequest) {
        ItemEntity isItemExists =
                itemRepository.findByPhoto(lookUpItemRequest.getPhoto())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        return ItemDto.of(isItemExists);
    }
}
