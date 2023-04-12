package com.example.demo.app.domain.model.entity;

import javax.persistence.*;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "representative_id", nullable = false)
    private Representative representative;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // Getter, Setter, Constructor 생략
}