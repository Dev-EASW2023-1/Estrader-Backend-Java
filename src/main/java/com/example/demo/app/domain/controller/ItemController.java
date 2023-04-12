package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.ItemListDto;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @PostMapping("/add")
    public String addUserEntity(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String day
    ) {
        itemService.addUser(username,password,day);
        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String showUserList(
            Model model
    ) {
        List<ItemEntity> userList = itemRepository.findAll();
        model.addAttribute("userList", userList);

        return "user/list";
    }

    @GetMapping("/test")
    public ResponseEntity<ItemListDto> Test() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(itemService.findUser(), headers, HttpStatus.OK);
    }
}

