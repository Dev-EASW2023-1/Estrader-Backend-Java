package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class FcmFailureException extends UserException {
    public FcmFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}