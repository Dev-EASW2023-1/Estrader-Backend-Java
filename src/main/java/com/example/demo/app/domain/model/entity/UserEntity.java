package com.example.demo.app.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String residentNumber;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    private String corporateRegistrationNumber;

    @Column(nullable = false)
    private String fcmToken;

    @Builder
    public UserEntity(
            String userId,
            String password,
            String name,
            String residentNumber,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken
    ){
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
    }

    @Builder(builderMethodName = "login", builderClassName = "login")
    public UserEntity(
            Long id,
            String userId,
            String password,
            String name,
            String residentNumber,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken
    ){
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
    }
}
