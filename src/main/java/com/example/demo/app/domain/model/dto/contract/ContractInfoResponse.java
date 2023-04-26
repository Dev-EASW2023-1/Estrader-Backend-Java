package com.example.demo.app.domain.model.dto.contract;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ContractInfoResponse {

    @NotBlank
    private String biddingPeriod;

    @NotBlank
    private String caseNumber;

    @NotBlank
    private String minimumBidPrice;

    @NotBlank
    private String managementNumber;

    @Builder
    public ContractInfoResponse(String biddingPeriod, String caseNumber, String minimumBidPrice, String managementNumber) {
        this.biddingPeriod = biddingPeriod;
        this.caseNumber = caseNumber;
        this.minimumBidPrice = minimumBidPrice;
        this.managementNumber = managementNumber;
    }
}
