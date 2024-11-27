package com.nob.validation.validator;

import com.nob.utils.SpElUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.ParamKey;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#EXPRESSION}
 * <p>
 * Apply for all type
 * @author Truong Ngo
 * */
public class ExpressionValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object value = attribute.getValue();
        if (Objects.isNull(value)) return Result.valid();
        String expression = constraint.getParam(ParamKey.EXPRESSION, String.class);
        Object o = context.getValue();
        Boolean result = SpElUtils.resolve(expression, o, Boolean.class);
        if (Objects.isNull(result)) return Result.invalid("Invalid expression: " + expression);
        String message = constraint.getParam(ParamKey.MESSAGE, String.class);
        return result ? Result.valid() : Result.invalid(message);
    }
}
