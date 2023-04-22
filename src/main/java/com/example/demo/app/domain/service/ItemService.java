package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.ItemDto;
import com.example.demo.app.domain.model.dto.ItemListDto;
import com.example.demo.app.domain.model.dto.LookUpItemRequest;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public void addItem(
            String picture,
            String information,
            String period,
            String location,
            String reserveprice,
            String auctionperiod

    ) {
        ItemEntity item = ItemEntity.builder()
                .picture(picture)
                .information(information)
                .period(period)
                .location(location)
                .reserveprice(reserveprice)
                .auctionperiod(auctionperiod)
                .build();
        itemRepository.save(item);
    }

    public ItemListDto findItemList() {
        List<ItemEntity> listUser = itemRepository.findAll();

        List<ItemDto> listItemDto = listUser.stream()
                .map(m -> new ItemDto(m.getPicture(), m.getInformation(), m.getPeriod(), m.getLocation(), m.getReserveprice(),m.getAuctionperiod()))
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    public ItemDto lookupItem(LookUpItemRequest lookUpItemRequest) {
        Optional<ItemEntity> isItemExists =
                itemRepository.findByPicture(URLDecoder.decode(lookUpItemRequest.getPicture(), StandardCharsets.UTF_8));

        if(isItemExists.isEmpty()){
            throw new IllegalArgumentException();
        }

        return ItemDto.builder()
                .picture(isItemExists.get().getPicture())
                .information(isItemExists.get().getInformation())
                .period(isItemExists.get().getPeriod())
                .location(isItemExists.get().getLocation())
                .reserveprice(isItemExists.get().getReserveprice())
                .auctionperiod(isItemExists.get().getAuctionperiod())
                .build();
    }
}
