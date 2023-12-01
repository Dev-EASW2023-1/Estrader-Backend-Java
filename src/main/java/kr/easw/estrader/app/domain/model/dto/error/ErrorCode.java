package kr.easw.estrader.app.domain.model.dto.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CONTRACT_FAILURE("C001", "계약에 실패하였습니다.", false),
    LOGIN_FAILURE("C002", "로그인에 실패했습니다.", false),
    PASSWORD_MISMATCH("C003", "비밀번호가 다릅니다.", false),
    DUPLICATE_ID("C004", "아이디가 이미 존재합니다.", false),
    SIGNUP_FAILURE("C005", "회원가입에 실패하였습니다.", false),
    NOT_FOUND("C006", "doesn't exist.", false),
    FCM_FAILURE("C007", "전송에 실패했습니다.", false),
    INSUFFICIENT_PERMISSIONS("C008", "권한이 부족합니다.", false),
    NOT_FOUND2("C006", "몰라잇", false);
    private final String code;
    private final String message;
    private final Boolean isSuccess;
}