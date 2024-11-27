package com.nob.validation.validator;

import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#REQUIRED}
 * <p>
 * Property value must be not {@code null}. Apply for all type
 * @author Truong Ngo
 * */
public class RequiredValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object value = attribute.getValue();
        return Objects.nonNull(value) ? Result.valid() : Result.invalid(Message.REQUIRED);
    }
}

