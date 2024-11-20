package com.nob.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wrapper of {@link Constraint}
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Constraints {
    Constraint[] value() default {};
}
