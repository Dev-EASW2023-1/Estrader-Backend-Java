package com.example.demo.app.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private String information;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String reserveprice;

    @Column(nullable = false)
    private String auctionperiod;

    @Builder
    public ItemEntity(String picture, String information, String period, String location, String reserveprice, String auctionperiod) {
        this.picture = picture;
        this.information = information;
        this.period = period;
        this.location = location;
        this.reserveprice = reserveprice;
        this.auctionperiod = auctionperiod;
    }
}
