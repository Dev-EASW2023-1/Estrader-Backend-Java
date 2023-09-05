package com.example.demo.app.domain.service;

import com.example.demo.app.domain.auth.JwtToken;
import com.example.demo.app.domain.auth.JwtTokenProvider;
import com.example.demo.app.domain.auth.SupportSecurityConfig;
import com.example.demo.app.domain.exception.exceptions.LoginFailureException;
import com.example.demo.app.domain.model.dto.error.ErrorCode;
import com.example.demo.app.domain.model.dto.user.UserDto;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final SupportSecurityConfig supportSecurityConfig;

    @Transactional
    public JwtToken authenticate(String userId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이 때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    public UserDto userDetailsToDto(@AuthenticationPrincipal User user) {
        UserEntity userEntity =
                userRepository.findByUserId(user.getUsername())
                        .orElseThrow(() -> new LoginFailureException(ErrorCode.NOT_FOUND));

        return UserEntity.toUserDto(userEntity);
    }
}
