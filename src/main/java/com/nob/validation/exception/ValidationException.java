package com.nob.validation.exception;

import com.nob.validation.validation.EvaluationResult;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private EvaluationResult evaluationResult;

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }
}
