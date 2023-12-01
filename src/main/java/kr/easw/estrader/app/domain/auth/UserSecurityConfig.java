package kr.easw.estrader.app.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 기본적인 웹 보안 활성화
//@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션 사용을 위해 선언
@RequiredArgsConstructor
public class UserSecurityConfig {
    private final JwtAuthenticateFilter jwtAuthenticateFilter;
    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    @Bean
    protected SecurityFilterChain userFilterChain(
            HttpSecurity http
    ) throws Exception {
        // REST API 이므로 basic auth 및 csrf 보안을 사용하지 않는다는 설정
        http.cors().disable()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .authorizeRequests()
                // 인증 없이 접근 가능
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/user/**").hasAuthority("ROLE_MEMBER")
                .antMatchers("/realtor/**").hasAuthority("ROLE_MANAGER")
                .antMatchers("/item/**").hasAnyAuthority("ROLE_MEMBER", "ROLE_MANAGER")
                .antMatchers("/contract/**").hasAnyAuthority("ROLE_MEMBER", "ROLE_MANAGER")
            .and()
            // Stateless (세션 사용 X)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // UsernamePasswordAuthenticationFilter 에 도달하기 전에 JWT 인증을 위하여 직접 구현한 필터를 먼저 동작
            .addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(delegatedAuthenticationEntryPoint);

        return http.build();
    }
}