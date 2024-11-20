package com.nob.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker the parameter need to be validated in a marked method
 * @see Validated
 * @author Truong Ngo
 * */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {

    /**
     * Validation profile use for validation
     * */
    String profile() default "default";
}
