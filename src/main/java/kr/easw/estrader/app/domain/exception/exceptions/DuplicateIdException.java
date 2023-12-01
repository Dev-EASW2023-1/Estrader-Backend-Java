package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class DuplicateIdException extends UserException {
    public DuplicateIdException(ErrorCode errorCode) {
        super(errorCode);
    }
}