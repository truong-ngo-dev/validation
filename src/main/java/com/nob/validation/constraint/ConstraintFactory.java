package com.nob.validation.constraint;

import com.nob.utils.StringUtils;
import com.nob.validation.annotation.*;
import com.nob.validation.validator.constant.ParamKey;

import java.lang.Object;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConstraintFactory {

    private static final List<Class<? extends Annotation>> constraintAnnotationsType = new ArrayList<>();

    static {
        constraintAnnotationsType.add(Required.class);
        constraintAnnotationsType.add(Null.class);
        constraintAnnotationsType.add(Expression.class);
        constraintAnnotationsType.add(MinMax.class);
        constraintAnnotationsType.add(DecimalMinMax.class);
        constraintAnnotationsType.add(Alphanumeric.class);
        constraintAnnotationsType.add(NotAllowCharacter.class);
        constraintAnnotationsType.add(Url.class);
        constraintAnnotationsType.add(Email.class);
        constraintAnnotationsType.add(Regex.class);
        constraintAnnotationsType.add(NotBlank.class);
        constraintAnnotationsType.add(Past.class);
        constraintAnnotationsType.add(PastOrPresent.class);
        constraintAnnotationsType.add(Future.class);
        constraintAnnotationsType.add(FutureOrPresent.class);
        constraintAnnotationsType.add(AssertTrue.class);
        constraintAnnotationsType.add(AssertFalse.class);
        constraintAnnotationsType.add(NotEmpty.class);
        constraintAnnotationsType.add(com.nob.validation.annotation.Object.class);
        constraintAnnotationsType.add(Collection.class);
        constraintAnnotationsType.add(Size.class);
    }

    /**
     * Factory method for creating constraint from annotation
     * @param annotation constraint annotation
     * @return {@code Constraint} matched
     * @throws IllegalArgumentException if annotation is not a constraint type
     * */
    public static Constraint createConstraint(Annotation annotation) {
        if (annotation instanceof Required required) {
            return new Constraint(ConstraintType.REQUIRED, StringUtils.nonEmptyValue(required.condition()), null);
        }
        if (annotation instanceof Null nil) {
            return new Constraint(ConstraintType.NULL, StringUtils.nonEmptyValue(nil.condition()), null);
        }
        if (annotation instanceof Expression expression) {
            return new Constraint(
                    ConstraintType.EXPRESSION,
                    StringUtils.nonEmptyValue(expression.condition()),
                    Map.of(ParamKey.EXPRESSION, expression.expression(), ParamKey.MESSAGE, expression.message()));
        }
        if (annotation instanceof MinMax minMax) {
            Map<String, Object> params = new LinkedHashMap<>();
            if (minMax.min() != Long.MIN_VALUE) params.put(ParamKey.MIN, minMax.min());
            if (minMax.max() != Long.MAX_VALUE) params.put(ParamKey.MAX, minMax.max());
            return new Constraint(
                    ConstraintType.MIN_MAX,
                    StringUtils.nonEmptyValue(minMax.condition()),
                    params
            );
        }
        if (annotation instanceof DecimalMinMax minMax) {
            Map<String, Object> params = new LinkedHashMap<>();
            if (!minMax.min().trim().isEmpty()) params.put(ParamKey.MIN, minMax.min());
            if (!minMax.max().trim().isEmpty()) params.put(ParamKey.MAX, minMax.max());
            return new Constraint(
                    ConstraintType.DECIMAL_MIN_MAX,
                    StringUtils.nonEmptyValue(minMax.condition()),
                    params
            );
        }
        if (annotation instanceof Alphanumeric alphanumeric) {
            return new Constraint(ConstraintType.ALPHANUMERIC, StringUtils.nonEmptyValue(alphanumeric.condition()), null);
        }
        if (annotation instanceof NotAllowCharacter notAllowCharacter) {
            return new Constraint(
                    ConstraintType.ALPHANUMERIC,
                    StringUtils.nonEmptyValue(notAllowCharacter.condition()),
                    Map.of(ParamKey.CHARACTERS, List.of(notAllowCharacter.characters())));
        }
        if (annotation instanceof Url url) {
            return new Constraint(
                    ConstraintType.URL,
                    StringUtils.nonEmptyValue(url.condition()),
                    Map.of(ParamKey.SCHEMES, List.of(url.schemes())));
        }
        if (annotation instanceof Email email) {
            return new Constraint(ConstraintType.EMAIL, StringUtils.nonEmptyValue(email.condition()), null);
        }
        if (annotation instanceof Regex regex) {
            return new Constraint(
                    ConstraintType.PATTERN,
                    StringUtils.nonEmptyValue(regex.condition()),
                    Map.of(ParamKey.REGEX, List.of(regex.regex())));
        }
        if (annotation instanceof NotBlank notBlank) {
            return new Constraint(ConstraintType.NOT_BLANK, StringUtils.nonEmptyValue(notBlank.condition()), null);
        }
        if (annotation instanceof Past past) {
            return new Constraint(ConstraintType.PAST, StringUtils.nonEmptyValue(past.condition()), null);
        }
        if (annotation instanceof PastOrPresent poe) {
            return new Constraint(ConstraintType.PAST_OR_PRESENT, StringUtils.nonEmptyValue(poe.condition()), null);
        }
        if (annotation instanceof Future future) {
            return new Constraint(ConstraintType.FUTURE, StringUtils.nonEmptyValue(future.condition()), null);
        }
        if (annotation instanceof FutureOrPresent foe) {
            return new Constraint(ConstraintType.FUTURE_OR_PRESENT, StringUtils.nonEmptyValue(foe.condition()), null);
        }
        if (annotation instanceof AssertTrue assertTrue) {
            return new Constraint(ConstraintType.ASSERT_TRUE, StringUtils.nonEmptyValue(assertTrue.condition()), null);
        }
        if (annotation instanceof AssertFalse assertFalse) {
            return new Constraint(ConstraintType.ASSERT_FALSE, StringUtils.nonEmptyValue(assertFalse.condition()), null);
        }
        if (annotation instanceof NotEmpty notEmpty) {
            return new Constraint(ConstraintType.NOT_EMPTY, StringUtils.nonEmptyValue(notEmpty.condition()), null);
        }
        if (annotation instanceof com.nob.validation.annotation.Object object) {
            return new Constraint(ConstraintType.OBJECT_ATTRIBUTE, StringUtils.nonEmptyValue(object.condition()), Map.of(ParamKey.PROFILE, object.profile()));
        }
        if (annotation instanceof Collection collection) {
            return new Constraint(ConstraintType.COLLECTION_ATTRIBUTE, StringUtils.nonEmptyValue(collection.condition()), Map.of(ParamKey.ELEMENT_PROFILE, collection.elementProfile()));
        }
        if (annotation instanceof Size size) {
            Map<String, Object> params = new LinkedHashMap<>();
            if (size.min() != Long.MIN_VALUE) params.put(ParamKey.MIN, size.min());
            if (size.max() != Long.MAX_VALUE) params.put(ParamKey.MAX, size.max());
            return new Constraint(
                    ConstraintType.SIZE,
                    StringUtils.nonEmptyValue(size.condition()),
                    params
            );
        }
        throw new IllegalArgumentException("Invalid annotation: " + annotation);
    }


    /**
     * Check if annotation is Constraint type
     * @param annotationType type of annotation
     * @return true if annotation is one of constraint annotations type
     * */
    public static boolean isValidationAnnotation(Class<? extends Annotation> annotationType) {
        return constraintAnnotationsType.contains(annotationType);
    }
}
