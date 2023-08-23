package com.example.demo.app.domain.controller;

import com.example.demo.app.domain.model.dto.contract.*;
import com.example.demo.app.domain.model.dto.item.ItemDto;
import com.example.demo.app.domain.model.dto.item.ItemListDto;
import com.example.demo.app.domain.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/complete")
    public ResponseEntity<ContractResponse> pushMember(
            @RequestBody ContractRequest contractRequest
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(contractService.registerContract(contractRequest));
    }

    @PostMapping("/item")
    public ResponseEntity<ItemDto> findItem(
            @RequestBody ItemInContractDto itemInContractDto
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(contractService.findItemByContract(itemInContractDto));
    }

    // 테스트용 API (Contract 테이블 JPQL @Query 사용)
    @GetMapping("/test1/{userId}/{realtorId}/{itemId}")
    public ResponseEntity<ItemListDto> test1(
            @PathVariable("userId") String userId,
            @PathVariable("realtorId") String realtorId,
            @PathVariable("itemId") String itemId
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(contractService.ContractTest1(userId, realtorId, itemId));
    }

    // 테스트용 API (Contract 테이블 쿼리 메소드 사용)
    @GetMapping("/test2/{userId}/{realtorId}/{itemId}")
    public ResponseEntity<ItemDto> test2(
            @PathVariable("userId") String userId,
            @PathVariable("realtorId") String realtorId,
            @PathVariable("itemId") String itemId
    ) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(contractService.ContractTest2(userId, realtorId, itemId));
    }

    @PostMapping("/find-info")
    public ResponseEntity<ContractInfoResponse> findItemForPDF(
            @RequestBody ContractInfoRequest contractInfoRequest
    ){
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(contractService.findContractInfo(contractInfoRequest));
    }

    @GetMapping("/create-pdf")
    public ResponseEntity<Resource> createPdf(){
        Resource resource = contractService.createPdfResource();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + generateFileName("test") + "\"")
                .body(resource);
    }

    // 파일명에 아이디와 생성일자를 조합하여 생성
    private String generateFileName(String userId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String creationDate = currentDateTime.format(formatter);
        return userId + "_" + creationDate + ".pdf";
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}
