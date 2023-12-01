package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class RealtorSignupFailureException extends UserException {
    public RealtorSignupFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}