package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.UserListDto;
import com.example.demo.app.domain.model.entity.User;
import com.example.demo.app.domain.repository.UserRepository;
import com.example.demo.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.List;
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService UserService;

    @PostMapping("/add")
    public String addUserinfoEntity(
            @RequestParam String userid,
            @RequestParam String password,
            @RequestParam String residentid,
            @RequestParam String phonenum,
            @RequestParam String address
    ) {
        UserService.addUserinfo(userid,password, residentid, phonenum, address);
        return "redirect:/userinfo/list";
    }

    @GetMapping("/list")
    public String showUserinfoList(
            Model model
    ) {
        List<User> userinfoList = userRepository.findAll();
        model.addAttribute("userinfoList", userinfoList);

        return "userinfo/list";
    }

    @GetMapping("/test")
    public ResponseEntity<UserListDto> Test() {

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(UserService.findUserinfo(), headers, HttpStatus.OK);
    }
}
