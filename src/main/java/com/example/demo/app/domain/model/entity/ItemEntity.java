package com.example.demo.app.domain.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String caseNumber;

    @Column(nullable = false)
    private String court;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String minimumBidPrice;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private String biddingPeriod;

    @Column(nullable = false)
    private String itemType;

    private String note;

    @Column(nullable = false)
    private String managementNumber;

    @Builder
    public ItemEntity(String caseNumber, String court, String location, String minimumBidPrice, String photo, String biddingPeriod, String itemType, String note, String managementNumber) {
        this.caseNumber = caseNumber;
        this.court = court;
        this.location = location;
        this.minimumBidPrice = minimumBidPrice;
        this.photo = photo;
        this.biddingPeriod = biddingPeriod;
        this.itemType = itemType;
        this.note = note;
        this.managementNumber = managementNumber;
    }
}
