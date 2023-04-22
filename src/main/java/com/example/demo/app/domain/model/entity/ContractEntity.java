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

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    private RepresentativeEntity representative;

    @ManyToOne(fetch = FetchType.EAGER)
    private ItemEntity item;

    @Builder
    public ContractEntity(
            UserEntity user,
            RepresentativeEntity representative,
            ItemEntity item
    ) {
        this.user = user;
        this.representative = representative;
        this.item = item;
    }
}