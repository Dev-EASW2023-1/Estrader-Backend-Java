package kr.easw.estrader.app.domain.auth;

import kr.easw.estrader.app.domain.Enum.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupportAuthorityUtils {
    private final List<GrantedAuthority> MANAGER = AuthorityUtils.createAuthorityList(Role.ROLE_MANAGER.getAuthority(), Role.ROLE_GUEST.getAuthority());
    private final List<GrantedAuthority> MEMBER = AuthorityUtils.createAuthorityList(Role.ROLE_MEMBER.getAuthority(), Role.ROLE_GUEST.getAuthority());

    // 메모리 상 Role 기반 권한 정보 생성
    public List<GrantedAuthority> createAuthorities(String role) {
        if (role.equals(Role.ROLE_MANAGER.getAuthority())) {
            return MANAGER;
        }
        return MEMBER;
    }
}