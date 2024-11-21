package com.nob.validation.validation;

import com.nob.utils.TypeUtils;
import com.nob.validation.annotation.Valid;
import com.nob.validation.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {

    private final ValidationEvaluators evaluators;

    @Before(value = "@annotation(com.nob.validation.annotation.Validated)")
    public void evaluate(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof Valid valid) {
                    String profile = valid.profile();
                    Object param = args[i];
                    Class<?> clazz = TypeUtils.isCollection(param.getClass()) ?
                            TypeUtils.getParameterGenericElementType(method, Valid.class) :
                            param.getClass();
                    EvaluationResult result = evaluators.evaluate(param, clazz, profile);
                    if (!result.isValid()) {
                        throw new ValidationException(result);
                    }
                }
            }
        }
    }
}
