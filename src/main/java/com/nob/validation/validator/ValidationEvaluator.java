package com.nob.validation.validator;

import com.nob.utils.CollectionUtils;
import com.nob.utils.MapUtils;
import com.nob.utils.TypeUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.context.ContextBuilder;
import com.nob.validation.context.ObjectBasedContext;
import com.nob.validation.metadata.validation.AttributeValidationMetadata;
import com.nob.validation.metadata.validation.ObjectValidationMetadata;
import com.nob.validation.metadata.validation.ValidationMetadataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationEvaluator {

    /**
     * Provider for validation metadata
     * */
    private final ValidationMetadataProvider validationMetaProvider;


    /**
     * Builder for validation context
     * */
    private final ContextBuilder contextBuilder;


    private final ApplicationContext applicationContext;


    /**
     * Perform validate on object
     * @param o validation object
     * @param type obj type or element type (collection case)
     * @param profile profile for type
     * @return {@code Result} of validation
     * */
    public Result validate(Object o, Class<?> type, String profile) {
        String loadProfile = (Objects.isNull(profile)) ? "default" : profile;
        ObjectValidationMetadata metadata = validationMetaProvider.load(type, loadProfile);
        return TypeUtils.isCollection(o.getClass()) ?
                validateCollection(o, metadata) :
                validateObject(o, metadata);
    }


    /**
     * Utility method for {@link #validate(Object, Class, String)}
     * <p>
     * Evaluate collection type
     * @param o validation object
     * @param metadata validation metadata
     * @return {@code Result} of validation
     * */
    @SuppressWarnings("unchecked")
    private Result validateCollection(Object o, ObjectValidationMetadata metadata) {
        List<?> collection = new ArrayList<>(TypeUtils.getCollection(o));
        List<Result> rs = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            Result result = validateObject(collection.get(i), metadata);
            if (!result.isValid()) {
                Map<String, Object> message = MapUtils.appendKeyPrefix((Map<String, Object>) result.getMessage(), String.format("[%s].", i));
                result.setMessage(message);
            }
            rs.add(result);
        }
        boolean isValid = rs.stream().allMatch(Result::isValid);
        if (isValid) return Result.valid();
        List<Result> invalidResults = rs.stream().filter(r -> !r.isValid()).toList();
        Map<String, Object> message = invalidResults.stream()
                .map(r -> (Map<String, Object>) r.getMessage())
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Result.invalid(message);
    }


    /**
     * Utility method for {@link #validate(Object, Class, String)}
     * <p>
     * Evaluate object type
     * @param o validation object
     * @param metadata validation metadata
     * @return {@code Result} of validation
     * */
    private Result validateObject(Object o, ObjectValidationMetadata metadata) {
        ObjectBasedContext context = contextBuilder.build(o, metadata);
        List<AttributeValidationMetadata> attributes = metadata.getProperties();
        List<Result> results = new ArrayList<>();
        for (AttributeValidationMetadata attributeMeta : attributes) {
            Attribute attribute = context.getAttribute(attributeMeta.getName());
            Result result = validateAttribute(attribute, attributeMeta, context);
            results.add(result);
        }
        boolean valid = results.stream().allMatch(Result::isValid);
        if (valid) return Result.valid();
        List<Result> invalidResults = results.stream().filter(r -> !r.isValid()).toList();
        @SuppressWarnings("unchecked")
        Map<String, Object> message = invalidResults.stream()
                .map(r -> (Map<String, Object>) r.getMessage())
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Result.invalid(message);
    }


    /**
     * Evaluation of context attribute
     * @param attribute attribute to be evaluated
     * @param metadata validation metadata for attribute
     * @param context object that hold the attribute
     * @return {@code EvaluationResult} result of evaluation process
     * */
    private Result validateAttribute(Attribute attribute, AttributeValidationMetadata metadata, Context context) {
        ValidatorFactory validatorFactory = applicationContext.getBean(ValidatorFactory.class);
        List<Constraint> constraints = metadata.getConstraints();
        List<Result> results = new ArrayList<>();
        for (Constraint c : constraints) {
            Validator validator = validatorFactory.getValidator(c.getType());
            Result result = validator.validate(context, attribute, c);
            results.add(result);
        }
        return resolveResult(results, attribute);
    }


    /**
     * Utility method for {@link #validateAttribute(Attribute, AttributeValidationMetadata, Context)}
     * Resolve evaluation result of an attribute
     * @param results evaluation result base on list of criteria
     * @param attribute attribute to be evaluated
     * @return the resolved {@code Result}
     * */
    private Result resolveResult(List<Result> results, Attribute attribute) {
        boolean isValid = results.stream().allMatch(Result::isValid);
        if (isValid) return Result.valid();
        List<Result> invalidResults = results.stream().filter(r -> !r.isValid()).toList();

        return (TypeUtils.isValueBased(attribute.getJavaType())) ?
                resolveValueBasedAttribute(attribute, invalidResults) :
                resolveNonValueBasedAttribute(attribute, invalidResults);
    }


    /**
     * Utility method for {@link #resolveResult(List, Attribute)}
     * <p>
     * Resolve the validation result of literal attribute
     * @param attribute attribute to be evaluated
     * @param invalidResults the invalid result list of attribute base on criteria
     * @return {@code Result} represent combination result for attribute
     * */
    private Result resolveValueBasedAttribute(Attribute attribute, List<Result> invalidResults) {
        List<String> messages = invalidResults.stream().map(r -> (String) r.getMessage()).toList();
        Map<String, Object> messageBody = (messages.size() == 1) ?
                Map.of(attribute.getName(), messages.get(0)) :
                Map.of(attribute.getName(), messages);
        return Result.invalid(messageBody);
    }


    /**
     * Utility method for {@link #resolveResult(List, Attribute)}
     * <p>
     * Resolve the evaluation result of non-literal attribute
     * @param attribute attribute to be evaluated
     * @param invalidResults the invalid result list of attribute base on criteria
     * @return {@code EvaluationResult} represent combination result for attribute
     * */
    @SuppressWarnings("all")
    private Result resolveNonValueBasedAttribute(Attribute attribute, List<Result> invalidResults) {

        Map<String, Object> message = new LinkedHashMap<>();

        // Result of required, not empty, size
        List<Result> simpleResult = invalidResults
                .stream()
                .filter(r -> r.getMessage() instanceof String)
                .toList();

        if (!CollectionUtils.isNullOrEmpty(simpleResult)) {
            Object mess = simpleResult.size() == 1 ? simpleResult.get(0).getMessage() : simpleResult.stream().map(r -> r.getMessage()).toList();
            message.put(attribute.getName(), mess);
        }

        // Result of validating inner object
        Result mapResult = invalidResults.stream()
                .filter(r -> Map.class.isAssignableFrom(r.getMessage().getClass()))
                .findFirst().orElse(null);

        if (Objects.nonNull(mapResult)) {
            Map<String, Object> mess = MapUtils.appendKeyPrefix((Map<String, Object>) mapResult.getMessage(), attribute.getName());
            message.putAll(mess);
        }

        return Result.invalid(message);
    }
}
