package com.example.meetontest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class ValidatorException extends RuntimeException {
    public ValidatorException(String message) {
        super(message);
    }
}