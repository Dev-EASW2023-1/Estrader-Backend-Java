package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class RealtorDuplicateIdException extends UserException {
    public RealtorDuplicateIdException(ErrorCode errorCode) {
        super(errorCode);
    }
}