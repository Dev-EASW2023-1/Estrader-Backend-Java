package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.ContractRequest;
import com.example.demo.app.domain.model.dto.ContractResponse;
import com.example.demo.app.domain.model.dto.FcmRequest;
import com.example.demo.app.domain.model.dto.FcmResponse;
import com.example.demo.app.domain.service.ContractService;
import com.example.demo.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/complete")
    public ResponseEntity<ContractResponse> pushMember(
            @RequestBody ContractRequest contractRequest
    ) {
        return new ResponseEntity<>(
                contractService.registerContract(contractRequest),
                getJsonHeader(),
                HttpStatus.OK);
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}
