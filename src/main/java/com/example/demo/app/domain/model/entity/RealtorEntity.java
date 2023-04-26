package com.example.demo.app.domain.model.entity;

import com.example.demo.app.domain.model.util.HashUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "realtor")
public class RealtorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String realtorId;

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
    public RealtorEntity(String realtorId, String password, String name, String residentNumber, String phoneNumber, String address, String corporateRegistrationNumber, String fcmToken){
        this.realtorId = realtorId;
        this.password = HashUtil.sha256(password);
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
    }

    @Builder(builderMethodName = "login", builderClassName = "login")
    public RealtorEntity(Long id, String realtorId, String password, String name, String residentNumber, String phoneNumber, String address, String corporateRegistrationNumber, String fcmToken){
        this.id = id;
        this.realtorId = realtorId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
    }
}