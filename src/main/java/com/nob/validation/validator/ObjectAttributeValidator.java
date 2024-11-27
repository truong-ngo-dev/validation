package com.nob.validation.validator;

import com.nob.utils.MapUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.ParamKey;

import java.util.Map;
import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#OBJECT_ATTRIBUTE}
 * <p>
 * Apply for object type
 * @author Truong Ngo
 * */
public class ObjectAttributeValidator implements Validator {

    private final ValidationEvaluator delegate;

    public ObjectAttributeValidator(ValidationEvaluator delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object val = attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        String profile = constraint.getParam(ParamKey.PROFILE, String.class);
        Result r = delegate.validate(val, attribute.getJavaType(), profile);
        if (r.isValid()) return r;
        Map<String, Object> message = (Map<String, Object>) r.getMessage();
        message = MapUtils.appendKeyPrefix(message, ".");
        return Result.invalid(message);
    }
}
