package com.example.demo.app.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "contract")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    private RealtorEntity realtor;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemEntity item;

    @Builder
    public ContractEntity(
            UserEntity user,
            RealtorEntity realtor,
            ItemEntity item
    ) {
        this.user = user;
        this.realtor = realtor;
        this.item = item;
    }
}