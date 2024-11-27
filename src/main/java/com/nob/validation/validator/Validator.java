package com.nob.validation.validator;

import com.nob.utils.SpElUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.exception.ValidationMetadataException;

import java.util.Objects;

/**
 * Validator for constraint applied on property
 * @see Constraint
 * @author Truong Ngo
 * */
public interface Validator {

    /**
     * Validate constraint place on attribute
     * @param context context hold the validation attribute
     * @param attribute attribute context represent object property
     * @param constraint applied constraint
     * @return {@code Result} of constraint validation process
     * */
    default Result validate(Context context, Attribute attribute, Constraint constraint) {
        String condition = constraint.getCondition();
        if (Objects.isNull(condition)) return validate(context, attribute, constraint);
        Boolean applicable = SpElUtils.resolve(condition, context.getValue(), Boolean.class);
        if (Objects.isNull(applicable))
            throw new ValidationMetadataException("Condition '" + condition + "' is not resolvable!");
        return applicable ? validate(context, attribute, constraint) : Result.valid();
    }


     /**
     * Internal validation of constraint
     * @param context context hold the validation attribute
     * @param attribute attribute context represent object property
     * @param constraint applied constraint
     * @return {@code Result} of constraint validation process
     * */
    Result validateInternal(Context context, Attribute attribute, Constraint constraint);

}
