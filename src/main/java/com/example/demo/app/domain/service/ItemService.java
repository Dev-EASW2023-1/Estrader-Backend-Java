package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.ItemDto;
import com.example.demo.app.domain.model.dto.ItemListDto;
import com.example.demo.app.domain.model.entity.Item;
import com.example.demo.app.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public void addUser(
            String username,
            String password,
            String day

    ) {
        Item user = Item.builder()
                .picture(username)
                .information(password)
                .period(day)
                .build();
        itemRepository.save(user);

    }

    
    public ItemListDto findUser() {
        List<Item> listUser = itemRepository.findAll();

        if(listUser == null){
            throw new IllegalArgumentException();
        }

        List<ItemDto> listItemDto = listUser.stream()
                .map(m -> new ItemDto(m.getPicture(), m.getInformation(), m.getPeriod(), m.getLocation(), m.getReserveprice(),m.getAuctionperiod()))
                .collect(Collectors.toList());

        ItemListDto.builder()
                .itemDto(listItemDto)
                .build();

        return ItemListDto.builder().itemDto(listItemDto).build();
    }


}
