package com.example.demo.app.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String picture;

    private String information;

    private String period;

    @Builder
    public UserEntity(String picture, String information, String period) {
        this.picture = picture;
        this.information = information;
        this.period = period;
    }
}
