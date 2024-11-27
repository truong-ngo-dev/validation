package com.nob.validation.validator;

import com.nob.utils.NumberUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.validator.constant.ParamKey;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#DECIMAL_MIN_MAX}
 * <p>
 * Apply for {@code Decimal} or {@code Integer} type
 * @author Truong Ngo
 * */
public class DecimalMinMaxValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Number val = (Number) attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        String minVal = constraint.getParam(ParamKey.MIN, String.class);
        String maxVal = constraint.getParam(ParamKey.MAX, String.class);
        if (Objects.nonNull(minVal) && Objects.nonNull(maxVal)) {
            BigDecimal min = new BigDecimal(minVal);
            BigDecimal max = new BigDecimal(maxVal);
            boolean result = NumberUtils.compare(val, min) >= 0 && NumberUtils.compare(val, max) <= 0;
            return result ? Result.valid() : Result.invalid(String.format(Message.MIN_MAX, min, max));
        } else if (Objects.nonNull(minVal)) {
            BigDecimal min = new BigDecimal(minVal);
            boolean result = NumberUtils.compare(val, min) >= 0;
            return result ? Result.valid() : Result.invalid(String.format(Message.MIN, min));
        } else {
            BigDecimal max = new BigDecimal(maxVal);
            boolean result = NumberUtils.compare(val, max) <= 0;
            return result ? Result.valid() : Result.invalid(String.format(Message.MAX, max));
        }
    }

}
