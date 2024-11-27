package com.nob.validation.validator;

import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.validator.constant.ParamKey;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#PATTERN}
 * <p>
 * Apply for string type
 * @author Truong Ngo
 * */
public class PatternValidator implements Validator {
    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        String s = (String) attribute.getValue();
        if (Objects.isNull(s)) return Result.valid();
        String regex = constraint.getParam(ParamKey.REGEX, String.class);
        return s.matches(regex) ? Result.valid() : Result.invalid(String.format(Message.REGEX, regex));
    }
}
