package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.ItemDto;
import com.example.demo.app.domain.model.dto.ItemListDto;
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

    public ItemListDto findUser() {
        List<ItemEntity> listUser = itemRepository.findAll();

        if(listUser == null){
            throw new IllegalArgumentException();
        }

        List<ItemDto> listItemDto = listUser.stream()
                .map(m -> new ItemDto(m.getPicture(), m.getInformation(), m.getPeriod(), m.getLocation(), m.getReserveprice(),m.getAuctionperiod()))
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }
}
