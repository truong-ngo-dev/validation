package com.nob.validation.validator;

import com.nob.utils.MapUtils;
import com.nob.utils.TypeUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.CollectionTypeAttribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.ParamKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#OBJECT_ATTRIBUTE}
 * <p>
 * Apply for object type
 * @author Truong Ngo
 * */
public class CollectionAttributeValidator implements Validator {

    private final ValidationEvaluator delegate;

    public CollectionAttributeValidator(ValidationEvaluator delegate) {
        this.delegate = delegate;
    }

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object val = attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        String elementProfile = constraint.getParam(ParamKey.ELEMENT_PROFILE, String.class);
        Class<?> elementJavaType = ((CollectionTypeAttribute) attribute).getElementJavaType();
        return delegate.validate(val, elementJavaType, elementProfile);
    }
}
