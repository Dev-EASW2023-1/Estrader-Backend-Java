package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class SignupFailureException extends UserException {
    public SignupFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}