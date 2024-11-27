package com.nob.validation.validator;

import com.nob.utils.NumberUtils;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.validator.constant.ParamKey;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#MIN_MAX}
 * <p>
 * Apply for {@code Integer} type
 * @author Truong Ngo
 * */
public class MinMaxValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Number val = (Number) attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        Number min = (Number) constraint.getParam(ParamKey.MIN);
        Number max = (Number) constraint.getParam(ParamKey.MAX);
        if (Objects.nonNull(min) && Objects.nonNull(max)) {
            boolean result = NumberUtils.compare(val, min) >= 0 && NumberUtils.compare(val, max) <= 0;
            return result ? Result.valid() : Result.invalid(String.format(Message.MIN_MAX, min, max));
        } else if (Objects.nonNull(min)) {
            boolean result = NumberUtils.compare(val, min) >= 0;
            return result ? Result.valid() : Result.invalid(String.format(Message.MIN, min));
        } else {
            boolean result = NumberUtils.compare(val, max) <= 0;
            return result ? Result.valid() : Result.invalid(String.format(Message.MAX, max));
        }
    }

}
