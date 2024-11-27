package com.nob.validation.validator;


import com.nob.utils.TypeUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.validator.constant.ParamKey;

import java.util.Collection;
import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#SIZE}
 * <p>
 * Apply for collection and string type
 * @author Truong Ngo
 * */
public class SizeValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object value = attribute.getValue();
        if (Objects.isNull(value)) return Validator.Result.valid();
        Integer min = constraint.getParam(ParamKey.MIN, Integer.class);
        Integer max = constraint.getParam(ParamKey.MAX, Integer.class);
        if (Objects.nonNull(min) && Objects.nonNull(max)) {
            if (value instanceof String s) {
                int length = s.length();
                boolean result = length >= min && length <= max;
                return result ? Result.valid() : Result.invalid(String.format(Message.MIN_MAX, min, max));
            }
            if (TypeUtils.isCollection(value.getClass())) {
                Collection<?> collection = TypeUtils.getCollection(value);
                int length = collection.size();
                boolean result = length >= min && length <= max;
                return result ? Result.valid() : Result.invalid(String.format(Message.MIN_MAX_SIZE, min, max));
            }
            throw new IllegalArgumentException("Invalid value type: " + value.getClass());
        }
        if (Objects.nonNull(min)) {
            if (value instanceof String s) {
                int length = s.length();
                boolean result = length >= min;
                return result ? Result.valid() : Result.invalid(String.format(Message.MIN_SIZE, min));
            }
            if (TypeUtils.isCollection(value.getClass())) {
                Collection<?> collection = TypeUtils.getCollection(value);
                int length = collection.size();
                boolean result = length >= min;
                return result ? Result.valid() : Result.invalid(String.format(Message.MIN_SIZE, min));
            }
            throw new IllegalArgumentException("Invalid value type: " + value.getClass());
        }
        if (Objects.nonNull(max)) {
            if (value instanceof String s) {
                int length = s.length();
                boolean result = length <= max;
                return result ? Result.valid() : Result.invalid(String.format(Message.MAX_SIZE, max));
            }
            if (TypeUtils.isCollection(value.getClass())) {
                Collection<?> collection = TypeUtils.getCollection(value);
                int length = collection.size();
                boolean result = length <= max;
                return result ? Result.valid() : Result.invalid(String.format(Message.MAX_SIZE, max));
            }
            throw new IllegalArgumentException("Invalid value type: " + value.getClass());
        }
        throw new IllegalArgumentException("Invalid constraint format!");
    }
}
