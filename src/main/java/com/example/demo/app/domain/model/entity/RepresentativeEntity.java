package com.example.demo.app.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "representative")
public class RepresentativeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fcmToken;

    @Column(nullable = false)
    private String corporateRegistrationNumber;

    @Builder
    public RepresentativeEntity(String username, String password, String fcmToken, String corporateRegistrationNumber){
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
    }
}