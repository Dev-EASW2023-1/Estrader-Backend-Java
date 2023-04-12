package com.example.demo.app.domain.model.entity;

import javax.persistence.*;

@Entity
public class Representative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "corporate_registration_number")
    private String corporateRegistrationNumber;

    // Getter, Setter, Constructor 생략
}