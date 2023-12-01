package kr.easw.estrader.app.domain.exception.exceptions;

import kr.easw.estrader.app.domain.exception.UserException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;

public class ContractFailureException extends UserException {
    public ContractFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}