package com.example.demo.app.domain.exception.exceptions;

import com.example.demo.app.domain.exception.UserException;
import com.example.demo.app.domain.model.dto.error.ErrorCode;

public class SignupFailureException extends UserException {
    public SignupFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}