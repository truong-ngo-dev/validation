package com.nob.validation.validator;

import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#NOT_BLANK}
 * <p>
 * Apply for string type
 * @author Truong Ngo
 * */
public class NotBlankValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        String s = (String) attribute.getValue();
        if (Objects.isNull(s)) return Result.valid();
        return !s.trim().isEmpty() ? Result.valid() : Result.invalid(Message.NOT_BLANK);
    }
}
