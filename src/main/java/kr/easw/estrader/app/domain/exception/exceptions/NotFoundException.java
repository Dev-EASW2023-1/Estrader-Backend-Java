package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class NotFoundException extends UserException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}