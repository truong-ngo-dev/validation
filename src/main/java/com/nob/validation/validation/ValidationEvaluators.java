package com.nob.validation.validation;

import com.nob.utils.CollectionUtils;
import com.nob.utils.MapUtils;
import com.nob.utils.SpElUtils;
import com.nob.utils.TypeUtils;
import com.nob.validation.context.*;
import com.nob.validation.exception.ValidationException;
import com.nob.validation.metadata.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidationEvaluators {


    /**
     * Provider for validation metadata
     * */
    private final ValidationMetadataProvider validationMetaProvider;


    /**
     * Builder for validation context
     * */
    private final ContextBuilder contextBuilder;


    /**
     * Perform evaluation of validation object
     * @param o validation object
     * @param type validation object type
     * @param profile validation profile
     * @return {@code EvaluationResult} result of evaluation
     * */
    public EvaluationResult evaluate(Object o, Class<?> type, String profile) {
        String loadProfile = (Objects.isNull(profile)) ? "default" : profile;
        ObjectValidationMetadata validationMeta = validationMetaProvider.load(type, loadProfile);
        return TypeUtils.isCollection(o.getClass()) ?
                evaluateCollection(o, validationMeta) :
                evaluateObject(o, validationMeta);
    }

    @SuppressWarnings("unchecked")
    private EvaluationResult evaluateObject(Object o, ObjectValidationMetadata validationMeta) {
        DefaultContext context = contextBuilder.build(o, validationMeta);

        List<AttributeValidationMetadata> attributesMeta = validationMeta.getProperties();
        List<EvaluationResult> results = new ArrayList<>();
        for (AttributeValidationMetadata attributeMeta : attributesMeta) {
            Attribute attribute = context.getAttribute(attributeMeta.getName());
            EvaluationResult result = evaluate(attribute, attributeMeta, o);
            results.add(result);
        }

        boolean isValid = results.stream().allMatch(EvaluationResult::isValid);
        if (isValid) return EvaluationResult.success();
        List<EvaluationResult> invalidResults = results.stream().filter(r -> !r.isValid()).toList();
        Map<String, Object> message = invalidResults.stream()
                .map(r -> (Map<String, Object>) r.getMessage())
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return EvaluationResult.failure(message, null);
    }

    @SuppressWarnings("unchecked")
    private EvaluationResult evaluateCollection(Object o, ObjectValidationMetadata validationMeta) {
        List<?> collection = new ArrayList<>(TypeUtils.getCollection(o));
        List<EvaluationResult> rs = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            EvaluationResult evaluationResult = evaluateObject(collection.get(i), validationMeta);
            if (!evaluationResult.isValid()) {
                Map<String, Object> message = MapUtils.appendKeyPrefix((Map<String, Object>) evaluationResult.getMessage(), String.format("[%s].", i));
                evaluationResult.setMessage(message);
            }
            rs.add(evaluationResult);
        }
        boolean isValid = rs.stream().allMatch(EvaluationResult::isValid);
        if (isValid) return EvaluationResult.success();
        List<EvaluationResult> invalidResults = rs.stream().filter(r -> !r.isValid()).toList();
        Map<String, Object> message = invalidResults.stream()
                .map(r -> (Map<String, Object>) r.getMessage())
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e, r) -> r));
        return EvaluationResult.failure(message, null);
    }


    /**
     * Evaluation of context attribute
     * @param attribute attribute to be evaluated
     * @param validationMeta validation metadata for attribute
     * @param context object that hold the attribute
     * @return {@code EvaluationResult} result of evaluation process
     * */
    public EvaluationResult evaluate(Attribute attribute, AttributeValidationMetadata validationMeta, Object context) {
        List<ValidationCriteria> criteria = validationMeta.getCriteria();
        List<EvaluationResult> results = new ArrayList<>();
        for (ValidationCriteria c : criteria) {
            Boolean isApplicable = Objects.isNull(c.getCondition()) ?
                    Boolean.TRUE :
                    SpElUtils.resolve(c.getCondition(), context, Boolean.class);

            if (Objects.isNull(isApplicable))
                throw new ValidationException("Cannot resolve condition: " + c.getCondition() + "!");

            if (!isApplicable) {
                results.add(EvaluationResult.success());
                continue;
            }
            if (c.isComparison()) {
                EvaluationResult r = comparisonEvaluator.evaluate(attribute, c);
                results.add(r);
                continue;
            }
            if (c.isExpression()) {
                EvaluationResult r = expressionEvaluator.evaluate(attribute, c);
                results.add(r);
                continue;
            }
            if (c.isNested()) {
                EvaluationResult r = TypeUtils.isCollection(attribute.getJavaType()) ?
                        collectionEvaluator.evaluate(attribute, c) :
                        nestedObjsEvaluator.evaluate(attribute, c);
                results.add(r);
            }
        }

        return resolveResult(results, attribute);

    }


    /**
     * Utility method for {@link #evaluate(Attribute, AttributeValidationMetadata, Object)}
     * Resolve evaluation result of an attribute
     * @param results evaluation result base on list of criteria
     * @param attribute attribute to be evaluated
     * @return the resolved {@code EvaluationResult}
     * */
    private EvaluationResult resolveResult(List<EvaluationResult> results, Attribute attribute) {
        boolean isValid = results.stream().allMatch(EvaluationResult::isValid);
        if (isValid) return EvaluationResult.success();
        List<EvaluationResult> invalidResults = results.stream().filter(r -> !r.isValid()).toList();

        return (TypeUtils.isValueBased(attribute.getJavaType())) ?
                resolveValueBasedAttribute(attribute, invalidResults) :
                resolveNonValueBasedAttribute(attribute, invalidResults);
    }


    /**
     * Utility method for {@link #resolveResult(List, Attribute)}
     * <p>
     * Resolve the evaluation result of literal attribute
     * @param attribute attribute to be evaluated
     * @param invalidResults the invalid result list of attribute base on criteria
     * @return {@code EvaluationResult} represent combination result for attribute
     * */
    private EvaluationResult resolveValueBasedAttribute(Attribute attribute, List<EvaluationResult> invalidResults) {
        List<String> messages = invalidResults.stream().map(r -> r.getCriteria().getMessage()).toList();
        Map<String, Object> messageBody = (messages.size() == 1) ?
                Map.of(attribute.getName(), messages.get(0)) :
                Map.of(attribute.getName(), messages);
        return EvaluationResult.failure(messageBody, null);
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
    private EvaluationResult resolveNonValueBasedAttribute(Attribute attribute, List<EvaluationResult> invalidResults) {
        EvaluationResult innerResult = invalidResults
                .stream()
                .filter(r -> Objects.isNull(r.getCriteria()))
                .findFirst().orElse(null);

        List<EvaluationResult> nonInnerResult = invalidResults.stream()
                .filter(r -> Objects.nonNull(r.getCriteria()))
                .toList();

        if (Objects.isNull(innerResult)) {
            resolveValueBasedAttribute(attribute, nonInnerResult);
        }

        if (CollectionUtils.isNullOrEmpty(nonInnerResult)) {
            Map<String, Object> messages = (Map<String, Object>) innerResult.getMessage();
            Map<String, Object> messageBody = MapUtils.appendKeyPrefix(messages, attribute.getName());
            return EvaluationResult.failure(messageBody, null);
        }

        List<String> nonInnerMess = nonInnerResult.stream().filter(r -> Objects.nonNull(r.getCriteria())).map(r -> r.getCriteria().getMessage()).toList();
        Map<String, Object> nonInnerMap = Map.of(attribute.getName(), nonInnerMess);
        Map<String, Object> innerMessage = (Map<String, Object>) innerResult.getMessage();
        Map<String, Object> innerMap = MapUtils.appendKeyPrefix(innerMessage, attribute.getName());
        Map<String, Object> message = new LinkedHashMap<>(innerMap);
        message.putAll(nonInnerMap);
        return EvaluationResult.failure(message, null);
    }

    /**
     * Criteria evaluator
     * */
    @FunctionalInterface
    interface Evaluator {
        EvaluationResult evaluate(Attribute attribute, ValidationCriteria criteria) throws ValidationException;
    }

    /**
     * Comparison evaluator
     * */
    Evaluator comparisonEvaluator = (a, cr) -> {
        boolean result = OperationEvaluators.evaluate(a.getValue(), cr);
        return result ? EvaluationResult.success() : EvaluationResult.failure(cr.getMessage(), cr);
    };

    /**
     * Expression evaluator
     * */
    Evaluator expressionEvaluator = (a, cr) -> {
        Boolean result = SpElUtils.resolve(cr.getExpression(), a.getValue(), Boolean.class);
        return Boolean.TRUE.equals(result) ? EvaluationResult.success() : EvaluationResult.failure(cr.getMessage(), cr);
    };

    /**
     * Collection value type evaluator
     * */
    @SuppressWarnings("unchecked")
    Evaluator collectionEvaluator = (a, cr) -> {
        String profile = cr.getProfile();
        CollectionTypeAttribute ca = ((CollectionTypeAttribute) a);
        Class<?> elementType = ca.getElementJavaType();
        List<DefaultContext> contexts = ca.getContexts();

        List<EvaluationResult> results = new ArrayList<>();
        for (int i = 0; i < contexts.size(); i++) {
            DefaultContext context = contexts.get(i);
            EvaluationResult result = evaluate(context.getValue(), elementType, profile);
            if (!result.isValid()) {
                String prefix = String.format("[%s].", i);
                Map<String, Object> oldMessage = (Map<String, Object>) result.getMessage();
                Map<String, Object> newMessage = MapUtils.appendKeyPrefix(oldMessage, prefix);
                result.setMessage(newMessage);
            }
            results.add(result);
        }

        boolean isValid = results.stream().allMatch(EvaluationResult::isValid);
        if (isValid) return EvaluationResult.success();
        List<EvaluationResult> invalidResults = results.stream().filter(r -> !r.isValid()).toList();
        Map<String, Object> message = invalidResults.stream()
                .map(r -> (Map<String, Object>) r.getMessage())
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return EvaluationResult.failure(message, null);
    };

    /**
     * Nested object value type evaluator
     * */
    @SuppressWarnings("unchecked")
    Evaluator nestedObjsEvaluator = (a, cr) -> {
        DefaultContext context = ((ObjectTypeAttribute) a).getContext();
        EvaluationResult result = evaluate(context.getValue(), a.getJavaType(), cr.getProfile());
        if (!result.isValid()) {
            Map<String, Object> oldMessage = (Map<String, Object>) result.getMessage();
            Map<String, Object> newMessage = MapUtils.appendKeyPrefix(oldMessage,".");
            result.setMessage(newMessage);
        }
        return result;
    };
}
