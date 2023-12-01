package kr.easw.estrader.app.domain.auth;

import kr.easw.estrader.app.domain.model.dto.Realtor.RealtorSignInResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;

// 로그인 시 비밀번호가 틀렸을 경우 예외 처리
@ControllerAdvice
public class LoginFailureExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RealtorSignInResponse> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.ok()
                .headers(getJson())
                .body(RealtorSignInResponse.builder()
                        .isSuccess(false)
                        .message(e.getMessage())
                        .build()
                );
    }

    private HttpHeaders getJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}