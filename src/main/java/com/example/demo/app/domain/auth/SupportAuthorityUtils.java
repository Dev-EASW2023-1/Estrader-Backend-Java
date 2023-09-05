package com.example.demo.app.domain.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.demo.app.domain.Enum.Role.*;

@Component
public class SupportAuthorityUtils {
    private final List<GrantedAuthority> MANAGER = AuthorityUtils.createAuthorityList(ROLE_MANAGER.getAuthority(), ROLE_GUEST.getAuthority());
    private final List<GrantedAuthority> MEMBER = AuthorityUtils.createAuthorityList(ROLE_MEMBER.getAuthority(), ROLE_GUEST.getAuthority());

    // 메모리 상 Role 기반 권한 정보 생성
    public List<GrantedAuthority> createAuthorities(String role) {
        if (role.equals(ROLE_MANAGER.getAuthority())) {
            return MANAGER;
        }
        return MEMBER;
    }
}