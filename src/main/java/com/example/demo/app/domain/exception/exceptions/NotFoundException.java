package com.example.demo.app.domain.exception.exceptions;

import com.example.demo.app.domain.exception.UserException;
import com.example.demo.app.domain.model.dto.error.ErrorCode;

public class NotFoundException extends UserException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}