package com.nob.validation.annotation;

import com.nob.validation.enumerate.FieldType;
import com.nob.validation.enumerate.Operator;

import java.lang.annotation.*;

/**
 * Indicate the validation criteria apply on property. Eg:
 * <blockquote><pre>
 *     {@code @Constraint(operator = OPERATOR.MIN_SIZE, compareValues = {"10"}, message = "must have at least 10 character")}
 *     private String name;
 * </pre></blockquote>
 * @author Truong Ngo
 * */
@Repeatable(Constraints.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {

    /**
     * Condition for apply validation
     * */
    String condition() default "";

    /**
     * Define type if property type is wildcard (eg: {@code java.lang.Object})
     * */
    FieldType type() default FieldType.STRING;

    /**
     * Comparison operator
     * */
    Operator operator() default Operator.EMPTY;

    /**
     * Compared values
     * */
    String[] comparedValues() default {};

    /**
     * SpEl expression for express the validation rule
     * */
    String expression() default "";

    /**
     * Profile validation file
     * */
    String profile() default "";

    /**
     * Error message when validation failed
     * */
    String message() default "";

}
