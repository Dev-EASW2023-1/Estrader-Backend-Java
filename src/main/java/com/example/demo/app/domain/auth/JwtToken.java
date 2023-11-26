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
//도메인이나 데이터패키지 별로 분류 -> 엔티티로 취급



//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//public class JwtToken {
//    private String grantType;
//    private String accessToken;
//    private String refreshToken;
//}
////도메인이나 데이터패키지 별로 분류 -> 엔티티로 취급