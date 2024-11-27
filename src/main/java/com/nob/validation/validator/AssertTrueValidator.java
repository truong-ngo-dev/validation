package com.nob.validation.validator;

import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#ASSERT_TRUE}
 * <p>
 * Property value must be {@code true}. Apply for {@code boolean} type
 * @author Truong Ngo
 * */
public class AssertTrueValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Boolean val = (Boolean) attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        return Boolean.TRUE.equals(val) ? Result.valid() : Result.invalid(Message.ASSERT_TRUE);
    }

}
