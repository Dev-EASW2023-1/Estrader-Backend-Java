package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.ItemDto;
import com.example.demo.app.domain.model.dto.ItemListDto;
import com.example.demo.app.domain.model.dto.LookUpItemRequest;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    // 아이템 파라미터 입력 페이지 출력
    @GetMapping("")
    public String inputPage(){
        return "item/index";
    }

    // 아이템 저장
    @PostMapping("/add")
    public String addUserEntity(
            @RequestParam String picture,
            @RequestParam String information,
            @RequestParam String period,
            @RequestParam String location,
            @RequestParam String reserveprice,
            @RequestParam String auctionperiod
    ) {
        itemService.addItem(
                picture,
                information,
                period,
                location,
                reserveprice,
                auctionperiod
        );
        return "redirect:/item/list";
    }

    // 아이템 리스트 출력
    @GetMapping("/list")
    public String showItemListForModel(
            Model model
    ) {
        List<ItemEntity> itemList = itemRepository.findAll();
        model.addAttribute("itemList", itemList);

        return "item/list";
    }

    @GetMapping("/show-list")
    public ResponseEntity<ItemListDto> showItemList() {
        return new ResponseEntity<>(itemService.findItemList(), getJsonHeader(), HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<ItemDto> showItem(LookUpItemRequest lookUpItemRequest) {
        return new ResponseEntity<>(itemService.lookupItem(lookUpItemRequest), getJsonHeader(), HttpStatus.OK);
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}

