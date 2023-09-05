package com.example.demo.app.domain.auth;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtToken(String grantType, String accessToken, String refreshToken){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}