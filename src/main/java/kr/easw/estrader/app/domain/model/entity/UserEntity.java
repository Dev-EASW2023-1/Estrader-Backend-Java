package kr.easw.estrader.app.domain.model.entity;

import kr.easw.estrader.app.domain.Enum.Role;
import kr.easw.estrader.app.domain.model.dto.Realtor.RealtorRegisterDataRequest;
import kr.easw.estrader.app.domain.model.dto.user.RegisterDataRequest;
import kr.easw.estrader.app.domain.model.dto.user.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

import static kr.easw.estrader.app.domain.Enum.Role.ROLE_MANAGER;
import static kr.easw.estrader.app.domain.Enum.Role.ROLE_MEMBER;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

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

    @Builder(builderMethodName = "userLogin", builderClassName = "userLogin")
    public UserEntity(
            String userId,
            String password,
            String name,
            String residentNumber,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken,
            String region
    ){
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.residentNumber = residentNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.fcmToken = fcmToken;
        this.region = region;
        this.role = ROLE_MEMBER;
    }

    @Builder(builderMethodName = "realtorLogin", builderClassName = "realtorLogin")
    public UserEntity(
            Long id,
            String userId,
            String password,
            String name,
            String residentNumber,
            String phoneNumber,
            String address,
            String corporateRegistrationNumber,
            String fcmToken,
            String region
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
        this.region = region;
        this.role = ROLE_MANAGER;
    }

    public UserEntity(UserEntity user, String fcmToken) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.residentNumber = user.getResidentNumber();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.corporateRegistrationNumber = user.getCorporateRegistrationNumber();
        this.fcmToken = fcmToken;
        this.region = user.getRegion();
        this.role = user.getRole();
    }

    public UserEntity(RegisterDataRequest dto, String password, Role role) {
        this.userId = dto.getUserId();
        this.password = password;
        this.name = dto.getName();
        this.residentNumber = dto.getResidentNumber();
        this.phoneNumber = dto.getPhoneNumber();
        this.address = dto.getAddress();
        this.corporateRegistrationNumber = dto.getCorporateRegistrationNumber();
        this.fcmToken = dto.getFcmToken();
        this.region = dto.getRegion();
        this.role = role;
    }

    public UserEntity(RealtorRegisterDataRequest dto, String password, Role role) {
        this.userId = dto.getRealtorId();
        this.password = password;
        this.name = dto.getName();
        this.residentNumber = dto.getResidentNumber();
        this.phoneNumber = dto.getPhoneNumber();
        this.address = dto.getAddress();
        this.corporateRegistrationNumber = dto.getCorporateRegistrationNumber();
        this.fcmToken = dto.getFcmToken();
        this.region = dto.getRegion();
        this.role = role;
    }

    public UserEntity(UserEntity user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.residentNumber = user.getResidentNumber();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.corporateRegistrationNumber = user.getCorporateRegistrationNumber();
        this.fcmToken = user.getFcmToken();
        this.region = user.getRegion();
        this.role = user.getRole();
    }

    public static UserEntity of(UserEntity user, String fcmToken) {
        return new UserEntity(user, fcmToken);
    }

    public static UserEntity of(RegisterDataRequest dto, String password) {
        return new UserEntity(dto, password, ROLE_MEMBER);
    }

    public static UserEntity of(RealtorRegisterDataRequest dto, String password) {
        return new UserEntity(dto, password, ROLE_MANAGER);
    }

    public static UserEntity of(UserEntity user) {
        return new UserEntity(user);
    }

    public static UserDto toUserDto(UserEntity user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .name(user.getName())
                .residentNumber(user.getResidentNumber())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .corporateRegistrationNumber(user.getCorporateRegistrationNumber())
                .fcmToken(user.getFcmToken())
                .region(user.getRegion())
                .build();
    }
}
