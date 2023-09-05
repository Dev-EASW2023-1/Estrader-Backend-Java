package com.example.demo.app.domain.auth;

import com.example.demo.app.domain.model.entity.UserEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;

@Slf4j
@Getter
public class SecurityUser extends User {
    public SecurityUser(UserEntity userEntity, SupportAuthorityUtils supportAuthorityUtils) {
        // AuthorityUtils 로 DB 에서 조회한 회원 정보를 이용해 Role 기반의 권한 정보(GrantedAuthority) 컬렉션 생성
        super(userEntity.getUserId(), userEntity.getPassword(), supportAuthorityUtils.createAuthorities(userEntity.getRole().toString()));

        log.info("SecurityUser member.username = {}", userEntity.getUserId());
        log.info("SecurityUser member.password = {}", userEntity.getPassword());
        log.info("SecurityUser member.role = {}", userEntity.getRole().toString());
    }
}
