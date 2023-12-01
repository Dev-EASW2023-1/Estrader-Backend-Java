package kr.easw.estrader.app.domain.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
// 한 번의 요청에 대해 정확히 한 번 실행되는 OnePerRequestFilter 사용
// 가입, 로그인, 재발급을 제외한 모든 Request 요청은 해당 필터 경유
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    // OncePerRequestFilter 를 상속하여 구현한 경우 doFilter 대신 doFilterInternal 메서드를 구현
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);

        // validateToken 으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 username, 권한을 뽑아 스프링 시큐리티 유저를 만들어 Authentication 반환
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context 에 '{}' 인증 정보를 저장했습니다", authentication.getName());
        } else {
            log.info("유효한 JWT 토큰이 없습니다");
        }

        filterChain.doFilter(request, response);
    }

    // 헤더 파싱
    private String getAuthorization(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = getAuthorization(request);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}