package kr.easw.estrader.app.domain.exception;

import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        log.error(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}