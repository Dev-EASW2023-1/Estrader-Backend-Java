package com.example.demo.app.domain.model.dto.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.net.URI;

@NoArgsConstructor
@Getter
public class ContractInfoResponse {
    @NotBlank
    private URI uri;


//    @NotBlank
//    private String biddingPeriod;
//
//    @NotBlank
//    private String caseNumber;
//
//    @NotBlank
//    private String minimumBidPrice;
//
//    @NotBlank
//    private String managementNumber;
//
//    @Builder
//    public ContractInfoResponse(String biddingPeriod, String caseNumber, String minimumBidPrice, String managementNumber) {
//        this.biddingPeriod = biddingPeriod;
//        this.caseNumber = caseNumber;
//        this.minimumBidPrice = minimumBidPrice;
//        this.managementNumber = managementNumber;
//    }
//
//    public ContractInfoResponse(ItemEntity item) {
//        this.caseNumber = item.getCaseNumber();
//        this.minimumBidPrice = item.getMinimumBidPrice();
//        this.biddingPeriod = item.getBiddingPeriod();
//        this.managementNumber = item.getManagementNumber();
//    }
//
//    public static ContractInfoResponse of(ItemEntity item) {
//        return new ContractInfoResponse(item);
//    }

    public ContractInfoResponse(URI uri) {
        this.uri = uri;
    }

    public static ContractInfoResponse of(URI uri) {
        return new ContractInfoResponse(uri);
    }
}
