package com.nob.validation.exception;

/**
 * Validation metadata exception
 * */
public class ValidationMetadataException extends RuntimeException {

    public ValidationMetadataException(String message) {
        super(message);
    }

    public ValidationMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
