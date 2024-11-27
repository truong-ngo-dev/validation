package com.nob.validation.validator;

import com.nob.utils.StringUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#EMAIL}
 * <p>
 * Apply for {@code String} type
 * @author Truong Ngo
 * */
public class EmailValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        String email = (String) attribute.getValue();
        if (Objects.isNull(email)) return Result.valid();
        boolean result = StringUtils.isEmail(email);
        return result ? Result.valid() : Result.invalid(Message.EMAIL);
    }
}
