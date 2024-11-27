package com.nob.validation.validator;

import com.nob.validation.constraint.ConstraintType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ValidatorFactory {

    private final ApplicationContext applicationContext;

    private final HashMap<ConstraintType, Validator> validators = new HashMap<>();

    @PostConstruct
    public void registerValidator() {

        ValidationEvaluator evaluator = applicationContext.getBean(ValidationEvaluator.class);

        validators.put(ConstraintType.REQUIRED, new RequiredValidator());
        validators.put(ConstraintType.NULL, new NullValidator());
        validators.put(ConstraintType.EXPRESSION, new ExpressionValidator());

        validators.put(ConstraintType.MIN_MAX, new MinMaxValidator());
        validators.put(ConstraintType.DECIMAL_MIN_MAX, new DecimalMinMaxValidator());

        validators.put(ConstraintType.ASSERT_TRUE, new AssertTrueValidator());
        validators.put(ConstraintType.ASSERT_FALSE, new AssertFalseValidator());

        validators.put(ConstraintType.EMAIL, new EmailValidator());
        validators.put(ConstraintType.NOT_BLANK, new NotBlankValidator());
        validators.put(ConstraintType.URL, new URLValidator());
        validators.put(ConstraintType.PATTERN, new PatternValidator());
        validators.put(ConstraintType.ALPHANUMERIC, new AlphanumericValidator());
        validators.put(ConstraintType.NOT_ALLOWED_CHARACTERS, new NotAllowedCharactersValidator());

        validators.put(ConstraintType.FUTURE, new FutureValidator());
        validators.put(ConstraintType.FUTURE_OR_PRESENT, new FutureOrPresentValidator());
        validators.put(ConstraintType.PAST, new PastValidator());
        validators.put(ConstraintType.PAST_OR_PRESENT, new PastOrPresentValidator());

        validators.put(ConstraintType.SIZE, new SizeValidator());
        validators.put(ConstraintType.NOT_EMPTY, new NotEmptyValidator());
        validators.put(ConstraintType.OBJECT_ATTRIBUTE, new ObjectAttributeValidator(evaluator));
        validators.put(ConstraintType.COLLECTION_ATTRIBUTE, new CollectionAttributeValidator(evaluator));
    }


    public Validator getValidator(ConstraintType constraintType) {
        return validators.get(constraintType);
    }
}
