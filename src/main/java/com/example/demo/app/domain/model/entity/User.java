package com.example.demo.app.domain.model.entity;

import com.example.demo.app.domain.model.util.HashUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userid;

    private String password;

    private String residentid;

    private String phonenum;

    private String address;
    public void setPassword(String password) {
        this.password = HashUtil.sha256(password);
    }

    public void setUserid(String userid) {
        this.userid = HashUtil.sha256(userid);
    }

    public void setResidentid(String residentid) {
        this.residentid = HashUtil.sha256(residentid);
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = HashUtil.sha256(phonenum);
    }


    @Builder
    public User(String userid, String password, String residentid, String phonenum, String address){
        this.userid = HashUtil.sha256(userid);
        this.password = HashUtil.sha256(password);
        this.residentid = HashUtil.sha256(residentid);
        this.phonenum = HashUtil.sha256(phonenum);
        this.address = HashUtil.sha256(address);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(HashUtil.sha256(password));
    }

    public boolean checkUserid(String userid) {
        return this.userid.equals(HashUtil.sha256(userid));
    }

    public boolean checkResidentid(String residentid) {
        return this.residentid.equals(HashUtil.sha256(residentid));
    }

    public boolean checkPhonenum(String phonenum) {
        return this.phonenum.equals(HashUtil.sha256(phonenum));
    }

}
