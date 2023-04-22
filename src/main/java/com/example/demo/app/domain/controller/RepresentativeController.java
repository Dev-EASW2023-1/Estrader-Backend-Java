package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.FcmRequest;
import com.example.demo.app.domain.model.dto.FcmResponse;
import com.example.demo.app.domain.model.dto.RepresentativeSignInRequest;
import com.example.demo.app.domain.model.dto.RepresentativeSignInResponse;
import com.example.demo.app.domain.model.entity.RepresentativeEntity;
import com.example.demo.app.domain.repository.RepresentativeRepository;
import com.example.demo.app.domain.service.RepresentativeService;
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
@RequestMapping("/representative")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;
    private final RepresentativeRepository representativeRepository;

    @GetMapping("")
    public String inputPage(){
        return "representative/index";
    }

    @PostMapping("/add")
    public String addRepresentativeInfo(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String fcmToken,
            @RequestParam String corporateRegistrationNumber
    ) {
        representativeService.addRepresentativeInfo(username, password, fcmToken, corporateRegistrationNumber);

        return "redirect:/representative/list";
    }

    @GetMapping("/list")
    public String showListRepresentativeInfo(
            Model model
    ) {
        List<RepresentativeEntity> representativeInfoList = representativeRepository.findAll();
        model.addAttribute("representativeInfoList", representativeInfoList);

        return "representative/list";
    }

    @PatchMapping("/login")
    public ResponseEntity<RepresentativeSignInResponse> loginMember(
            @RequestBody RepresentativeSignInRequest representativeSignInRequest
    ) {
        return new ResponseEntity<>(
                representativeService.loginRepresentative(representativeSignInRequest),
                getJsonHeader(),
                HttpStatus.OK);
    }

    @PostMapping("/fcm")
    public ResponseEntity<FcmResponse> pushMember(
            @RequestBody FcmRequest fcmRequest
    ) {
        return new ResponseEntity<>(
                representativeService.sendByToken(fcmRequest),
                getJsonHeader(),
                HttpStatus.OK);
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}
