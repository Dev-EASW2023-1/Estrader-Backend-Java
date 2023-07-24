package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.item.ItemDto;
import com.example.demo.app.domain.model.dto.item.ItemListDto;
import com.example.demo.app.domain.model.dto.item.LookUpItemRequest;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public String addItem(
            @RequestParam String caseNumber,
            @RequestParam String court,
            @RequestParam String location,
            @RequestParam String minimumBidPrice,
            @RequestParam String photo,
            @RequestParam String biddingPeriod,
            @RequestParam String itemType,
            @RequestParam String note,
            @RequestParam String managementNumber
    ) {
        itemService.addItem(
                caseNumber,
                court,
                location,
                minimumBidPrice,
                photo,
                biddingPeriod,
                itemType,
                note,
                managementNumber
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
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(itemService.findItemList());
    }

    @PostMapping("/show")
    public ResponseEntity<ItemDto> showItem(@RequestBody LookUpItemRequest lookUpItemRequest) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(itemService.lookUpItem(lookUpItemRequest));
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}

