package com.nob.validation.metadata.validation;

import com.nob.validation.enumerate.Operator;
import lombok.Data;

import java.util.Objects;

/**
 * Validation criteria
 * */
@Data
public class ValidationCriteria {

    /**
     * Condition for apply criteria
     * */
    private String condition;

    /**
     * Compare operator
     * */
    private Operator operator;

    /**
     * Compare values
     * */
    private String[] comparedValues;

    /**
     * Pattern for compared string date
     * */
    private String datePattern;

    /**
     * Expression express criteria
     * */
    private String expression;

    /**
     * Error message
     * */
    private String message;

    /**
     * Profile for nested object, collection attribute
     * */
    private String profile;

    /**
     * Is expression criteria
     * */
    public boolean isExpression() {
        return Objects.nonNull(expression);
    }

    /**
     * Is comparison type
     * */
    public boolean isComparison() {
        return Objects.nonNull(operator);
    }

    /**
     * Is nested object type (object, collection)
     * */
    public boolean isNested() {
        return Objects.nonNull(profile);
    }

}
