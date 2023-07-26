package com.example.demo.app.domain.model.entity;

import com.example.demo.app.domain.model.util.HashUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.time.LocalDateTime;

@Log4j2
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

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    /**
     * 엔티티가 저장될 때마다 lastUpdatedAt 필드에 현재 시간이 자동으로 설정
     */

    // INSERT QUERY 전에 실행, 회원가입 시 실행
    @PrePersist
    public void prePersist() {
        log.info("prePersist");
        lastUpdatedAt = LocalDateTime.now();
    }

    // before second save 전에 실행, 로그인 시 실행
    @PreUpdate
    public void preUpdate() {
        log.info("preUpdate");
        lastUpdatedAt = LocalDateTime.now();
    }

    @Builder
    public RealtorEntity(String realtorId, String password, String name, String residentNumber, String phoneNumber, String address, String corporateRegistrationNumber, String fcmToken, String region){
        this.realtorId = realtorId;
        this.password = HashUtil.sha256(password);
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
        this.region = region;
    }

    @Builder(builderMethodName = "login", builderClassName = "login")
    public RealtorEntity(Long id, String realtorId, String password, String name, String residentNumber, String phoneNumber, String address, String corporateRegistrationNumber, String fcmToken, String region){
        this.id = id;
        this.realtorId = realtorId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
        this.region = region;
    }

    public static RealtorEntity toEntity(RealtorEntity realtor, String password) {
        return RealtorEntity.login()
                .id(realtor.getId())
                .realtorId(realtor.getRealtorId())
                .password(password)
                .name(realtor.getName())
                .residentNumber(realtor.getResidentNumber())
                .phoneNumber(realtor.getPhoneNumber())
                .address(realtor.getAddress())
                .corporateRegistrationNumber(realtor.getCorporateRegistrationNumber())
                .fcmToken(realtor.getFcmToken())
                .region(realtor.getRegion())
                .build();
    }
}