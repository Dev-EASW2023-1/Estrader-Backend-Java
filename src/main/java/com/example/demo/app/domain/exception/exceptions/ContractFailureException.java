package com.example.demo.app.domain.exception.exceptions;

import com.example.demo.app.domain.exception.UserException;
import com.example.demo.app.domain.model.dto.error.ErrorCode;

public class ContractFailureException extends UserException {
    public ContractFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}