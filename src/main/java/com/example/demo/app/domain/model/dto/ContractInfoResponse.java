package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ContractInfoResponse {

    @NotBlank
    private String auctionperiod;
    @NotBlank
    private String information;
    @NotBlank
    private String reserveprice;

    @Builder
    public ContractInfoResponse(String auctionperiod, String information, String reserveprice) {
        this.auctionperiod = auctionperiod;
        this.information = information;
        this.reserveprice = reserveprice;
    }
}
