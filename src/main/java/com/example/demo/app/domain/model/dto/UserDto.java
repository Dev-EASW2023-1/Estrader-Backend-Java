package com.example.demo.app.domain.model.dto;


import com.example.demo.app.domain.model.entity.User;
import com.example.demo.app.domain.model.util.HashUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Getter
@NoArgsConstructor
public class UserDto {
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
    public UserDto(String userid, String password, String residentid, String phonenum, String address) {
        this.userid = HashUtil.sha256(userid);
        this.password = HashUtil.sha256(password);
        this.residentid = HashUtil.sha256(residentid);
        this.phonenum = HashUtil.sha256(phonenum);
        this.address = HashUtil.sha256(address);

    }

    public String getUserid() {

        return HashUtil.sha256(userid);
    }

    public String getPassword() {
        return HashUtil.sha256(password);
    }

    public String getResidentid() {
        return HashUtil.sha256(residentid);
    }

    public String getPhonenum() {
        return HashUtil.sha256(phonenum);
    }

    public String getAddress() {
        return HashUtil.sha256(address);
    }
    public User toEntity() {
        return User.builder()
                .userid(userid)
                .password(password)
                .residentid(residentid)
                .phonenum(phonenum)
                .address(address)
                .build();
    }
}
