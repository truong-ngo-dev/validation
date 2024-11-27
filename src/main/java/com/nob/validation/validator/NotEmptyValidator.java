package com.nob.validation.validator;

import com.nob.utils.TypeUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;

import java.util.Collection;
import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#NOT_EMPTY}
 * <p>
 * Apply for collection type
 * @author Truong Ngo
 * */
public class NotEmptyValidator implements Validator {
    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object value = attribute.getValue();
        if (Objects.isNull(value)) return Result.valid();
        Collection<?> collection = TypeUtils.getCollection(value);
        return !collection.isEmpty() ? Result.valid() : Result.invalid(Message.NOT_EMPTY);
    }
}
