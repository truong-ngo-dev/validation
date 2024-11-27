package com.nob.validation.validator;

import com.nob.utils.StringUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#ALPHANUMERIC}
 * <p>
 * Apply for string type
 * @author Truong Ngo
 * */
public class AlphanumericValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        String s = (String) attribute.getValue();
        if (Objects.isNull(s)) return Result.valid();
        boolean valid = StringUtils.isAlphanumeric(s);
        return valid ? Result.valid() : Result.invalid(Message.ALPHANUMERIC);
    }
}
