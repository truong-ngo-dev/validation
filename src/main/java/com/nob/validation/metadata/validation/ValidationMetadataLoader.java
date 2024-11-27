package com.nob.validation.metadata.validation;

import com.nob.validation.constraint.ConstraintFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Load the validation metadata based on annotated object
 * @author Truong Ngo
 * */
@Component
@RequiredArgsConstructor
public class ValidationMetadataLoader {

    /**
     * Validation metadata provider
     * */
    private final ValidationMetadataProvider metaProvider;


    /**
     * Read object attribute and create validation metadata
     * @param clazz object type
     * @param profile declare validation metadata profile
     * */
    public void loadValidationMeta(Class<?> clazz, String profile) {
        if (metaProvider.contains(clazz, profile)) return;
        ObjectValidationMetadata ov = new ObjectValidationMetadata();
        ov.setJavaType(clazz);
        ov.setProfile(profile);
        metaProvider.addValidation(ov);
        Field[] fields = clazz.getDeclaredFields();
        List<AttributeValidationMetadata> attributeValidations = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            AttributeValidationMetadata attributeValidation = loadAttributeValidationMeta(field);
            if (Objects.nonNull(attributeValidation)) attributeValidations.add(attributeValidation);
        }
        ov.setProperties(attributeValidations);
    }


    /**
     * Read all annotation of attribute to create {@code Constraint} object
     * @param field attribute reflection object
     * @return {@code AttributeValidationMetadata} represent constraints apply on attribute
     * */
    public AttributeValidationMetadata loadAttributeValidationMeta(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations.length == 0) return null;
        List<com.nob.validation.constraint.Constraint> constraints = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (ConstraintFactory.isValidationAnnotation(annotation.annotationType())) {
                com.nob.validation.constraint.Constraint constraint = ConstraintFactory.createConstraint(annotation);
                constraints.add(constraint);
            }
        }
        if (constraints.isEmpty()) return null;
        AttributeValidationMetadata av = new AttributeValidationMetadata();
        av.setName(field.getName());
        av.setJavaType(field.getType().isArray() ? List.class : field.getType());
        av.setConstraints(constraints);
        return av;
    }
}
