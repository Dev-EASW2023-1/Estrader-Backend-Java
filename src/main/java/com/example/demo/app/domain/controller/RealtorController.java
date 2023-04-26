package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.fcm.FcmRequest;
import com.example.demo.app.domain.model.dto.fcm.FcmResponse;
import com.example.demo.app.domain.model.dto.Realtor.RealtorSignInRequest;
import com.example.demo.app.domain.model.dto.Realtor.RealtorSignInResponse;
import com.example.demo.app.domain.model.entity.RealtorEntity;
import com.example.demo.app.domain.repository.RealtorRepository;
import com.example.demo.app.domain.service.RealtorService;
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
@RequestMapping("/realtor")
@RequiredArgsConstructor
public class RealtorController {

    private final RealtorService realtorService;
    private final RealtorRepository realtorRepository;

    @GetMapping("")
    public String inputPage(){
        return "realtor/index";
    }

    @PostMapping("/add")
    public String addRealtorInfo(
            @RequestParam String userId,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String residentNumber,
            @RequestParam String phoneNumber,
            @RequestParam String address,
            @RequestParam String corporateRegistrationNumber,
            @RequestParam String fcmToken
    ) {
        realtorService.addRealtorInfo(
                userId,
                password,
                name,
                residentNumber,
                phoneNumber,
                address,
                corporateRegistrationNumber,
                fcmToken
        );

        return "redirect:/realtor/list";
    }

    @GetMapping("/list")
    public String showListRealtorInfo(
            Model model
    ) {
        List<RealtorEntity> realtorInfoList = realtorRepository.findAll();
        model.addAttribute("realtorInfoList", realtorInfoList);

        return "realtor/list";
    }

    @PatchMapping("/login")
    public ResponseEntity<RealtorSignInResponse> loginMember(
            @RequestBody RealtorSignInRequest realtorSignInRequest
    ) {
        return new ResponseEntity<>(
                realtorService.loginRealtor(realtorSignInRequest),
                getJsonHeader(),
                HttpStatus.OK);
    }

    @PostMapping("/fcm")
    public ResponseEntity<FcmResponse> pushMember(
            @RequestBody FcmRequest fcmRequest
    ) {
        return new ResponseEntity<>(
                realtorService.sendByToken(fcmRequest),
                getJsonHeader(),
                HttpStatus.OK);
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}
