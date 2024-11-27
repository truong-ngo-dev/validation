package com.nob.validation.exception;

import com.nob.validation.validator.Result;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private Result result;

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Result result) {
        this.result = result;
    }
}
