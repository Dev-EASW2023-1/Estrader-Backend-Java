package com.example.demo.app.domain.model.dto;

import com.example.demo.app.domain.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RegisterDataRequest {
    @NotBlank
    private String userid;

    @NotBlank
    private String password;

    @NotBlank
    private String residentid;

    @NotBlank
    private String phonenum;

    @NotBlank
    private String address;

    @Builder
    public RegisterDataRequest(String userid, String password, String residentid, String phonenum, String address) {
        this.userid = userid;
        this.password = password;
        this.residentid = residentid;
        this.phonenum = phonenum;
        this.address = address;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userid(userid)
                .password(password)
                .residentid(residentid)
                .phonenum(phonenum)
                .address(address)
                .build();
    }
}
