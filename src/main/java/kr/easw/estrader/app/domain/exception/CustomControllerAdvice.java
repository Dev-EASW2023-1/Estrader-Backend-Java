package kr.easw.estrader.app.domain.exception;

import kr.easw.estrader.app.domain.exception.exceptions.*;
import kr.easw.estrader.app.domain.model.dto.Realtor.RealtorRegisterDataResponse;
import kr.easw.estrader.app.domain.model.dto.Realtor.RealtorSignInResponse;
import kr.easw.estrader.app.domain.model.dto.Realtor.RealtorSignupCheckResponse;
import kr.easw.estrader.app.domain.model.dto.contract.ContractResponse;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import kr.easw.estrader.app.domain.model.dto.fcm.FcmResponse;
import kr.easw.estrader.app.domain.model.dto.user.RegisterDataResponse;
import kr.easw.estrader.app.domain.model.dto.user.SignInResponse;
import kr.easw.estrader.app.domain.model.dto.user.SignupCheckResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.charset.StandardCharsets;

// @ControllerAdvice 는 모든 @Controller 즉, 전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation
// @RestController 에서 예외가 발생하든 @Controller 에서 예외가 발생하든 @ControllerAdvice + @ExceptionHandler 조합으로 다 캐치 가능
@ControllerAdvice
public class CustomControllerAdvice {

    // Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<FcmResponse> NotExistDataHandle(
            NotFoundException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(FcmResponse.of(errorCode));
    }

    // FCM 전송 실패
    @ExceptionHandler(FcmFailureException.class)
    public ResponseEntity<FcmResponse> FcmFailureHandle(
            FcmFailureException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(FcmResponse.of(errorCode));
    }

    // 사용자 로그인 실패, 비밀번호 불일치
    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<SignInResponse> LoginFailureHandle(
            LoginFailureException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(SignInResponse.of(errorCode));
    }

    // 사용자 회원가입 실패
    @ExceptionHandler(SignupFailureException.class)
    public ResponseEntity<RegisterDataResponse> SignupFailureHandle(
            SignupFailureException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(RegisterDataResponse.of(errorCode));
    }

    // 사용자 아이디 중복
    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<SignupCheckResponse> DuplicateIdHandle(
            DuplicateIdException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(SignupCheckResponse.of(errorCode));
    }

    // 대리인 로그인 실패, 비밀번호 불일치
    @ExceptionHandler(RealtorLoginFailureException.class)
    public ResponseEntity<RealtorSignInResponse> RealtorLoginFailureHandle(
            RealtorLoginFailureException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(RealtorSignInResponse.of(errorCode));
    }

    // 대리인 회원가입 실패
    @ExceptionHandler(RealtorSignupFailureException.class)
    public ResponseEntity<RealtorRegisterDataResponse> RealtorSignupFailureHandle(
            RealtorSignupFailureException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(RealtorRegisterDataResponse.of(errorCode));
    }

    // 대리인 아이디 중복
    @ExceptionHandler(RealtorDuplicateIdException.class)
    public ResponseEntity<RealtorSignupCheckResponse> RealtorDuplicateIdHandle(
            RealtorDuplicateIdException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(RealtorSignupCheckResponse.of(errorCode));
    }

    // 계약 실패
    @ExceptionHandler(ContractFailureException.class)
    public ResponseEntity<ContractResponse> DuplicateIdHandle(
            ContractFailureException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.ok()
                .headers(getJson())
                .body(ContractResponse.of(errorCode));
    }

    private HttpHeaders getJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}