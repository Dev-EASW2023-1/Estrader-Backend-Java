package com.example.demo.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String 물건사진;

    private String 물건정보;

    private String 입찰기간;


    @Builder
    public User(String username, String password, String day) {
        this.물건사진 = username;
        this.물건정보 = password;
        this.입찰기간 = day;
    }
}
