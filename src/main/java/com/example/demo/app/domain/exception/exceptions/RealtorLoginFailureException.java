package com.example.demo.app.domain.exception.exceptions;

import com.example.demo.app.domain.exception.UserException;
import com.example.demo.app.domain.model.dto.error.ErrorCode;

public class RealtorLoginFailureException extends UserException {
    public RealtorLoginFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}
