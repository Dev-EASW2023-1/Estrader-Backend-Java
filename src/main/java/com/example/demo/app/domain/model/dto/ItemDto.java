package com.example.demo.app.domain.model.dto;

import com.example.demo.app.domain.model.entity.ItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ItemDto {
    @NotBlank
    private String picture;

    @NotBlank
    private String information;

    @NotBlank
    private String period;

    @NotBlank
    private String location;

    @NotBlank
    private String reserveprice;

    @NotBlank
    private String auctionperiod;

    @Builder
    public ItemDto(String picture, String information, String period, String location, String reserveprice, String auctionperiod){
        this.picture = picture;
        this.information = information;
        this.period = period;
        this.location = location;
        this.reserveprice = reserveprice;
        this.auctionperiod = auctionperiod;
    }

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .picture(picture)
                .information(information)
                .period(period)
                .location(location)
                .reserveprice(reserveprice)
                .auctionperiod(auctionperiod)
                .build();
    }
}


