package kr.easw.estrader.app.domain.Enum;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {

    ROLE_GUEST(ROLES.GUEST, "비회원"), ROLE_MANAGER(ROLES.MANAGER, "대리인"), ROLE_MEMBER(ROLES.MEMBER, "사용자");

    public static class ROLES {
        public static final String GUEST = "ROLE_GUEST";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MEMBER = "ROLE_MEMBER";
    }

    private final String authority;
    private final String description;

    Role(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }
}