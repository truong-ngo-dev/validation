package com.nob.validation.validator;

import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;

import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#ASSERT_FALSE}
 * <p>
 * Property must be {@code false}. Apply for {@code boolean} type
 * @author Truong Ngo
 * */
public class AssertFalseValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Boolean val = (Boolean) attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        return Boolean.FALSE.equals(val) ? Result.valid() : Result.invalid(Message.ASSERT_FALSE);
    }

}
