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
    @SuppressWarnings("unchecked")
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object val = attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        String elementProfile = constraint.getParam(ParamKey.ELEMENT_PROFILE, String.class);
        List<?> collection = List.of(TypeUtils.getCollection(val));
        CollectionTypeAttribute cta = (CollectionTypeAttribute) attribute;
        List<Result> results = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            Object item = collection.get(i);
            Result r = delegate.validate(item, cta.getElementJavaType(), elementProfile);
            if (!r.isValid()) {
                Map<String, Object> message = (Map<String, Object>) r.getMessage();
                message = MapUtils.appendKeyPrefix(message, String.format("[%s]." , i));
                r.setMessage(message);
            }
            results.add(r);
        }
        boolean valid = results.stream().allMatch(Result::isValid);
        if (valid) return Result.valid();
        List<Result> invalidResults = results.stream().filter(r -> !r.isValid()).toList();
        Map<String, Object> message = invalidResults.stream()
                .flatMap(r -> ((Map<String, Object>) r.getMessage()).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Result.invalid(message);
    }
}
