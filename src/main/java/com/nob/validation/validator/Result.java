package com.nob.validation.validator;

import lombok.Getter;
import lombok.Setter;

/**
 * Result of constraint validation
 * */
@Getter
@Setter
public class Result {

    /**
     * Valid instance of result
     * */
    private static final Result VALID = new Result(true, null);


    /**
     * Result of constraint validation
     * */
    private final boolean valid;


    /**
     * Error message
     * */
    private Object message;


    /**
     * Private constructor prevent outside usage
     * */
    private Result(boolean valid, Object message) {
        this.valid = valid;
        this.message = message;
    }


    /**
     * Create a valid instance
     * */
    public static Result valid() {
        return VALID;
    }


    /**
     * Create a invalid instance
     * */
    public static Result invalid(Object message) {
        return new Result(false, message);
    }
}
