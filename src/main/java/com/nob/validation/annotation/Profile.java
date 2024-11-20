package com.nob.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker the validation model. Use to generate the validation metadata and context metadata<p>
 * when application start
 * @see Constraint
 * @author Truong Ngo
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface Profile {

    /**
     * Define profile for validation meta for generation process
     * */
    String value() default "default";
}
