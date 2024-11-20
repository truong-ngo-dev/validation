package com.nob.validation.metadata.validation;

import com.nob.utils.StringUtils;
import com.nob.utils.TypeUtils;
import com.nob.validation.annotation.Constraint;
import com.nob.validation.enumerate.Operator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ValidationMetadataLoader {

    private final ValidationMetadataProvider metaProvider;

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

    public AttributeValidationMetadata loadAttributeValidationMeta(Field field) {
        Constraint[] annotations = field.getAnnotationsByType(Constraint.class);
        if (annotations.length == 0) return null;
        List<ValidationCriteria> criteria = new ArrayList<>();
        for (Constraint constraint : annotations) {
            ValidationCriteria c = new ValidationCriteria();
            c.setCondition(StringUtils.nonEmptyValue(constraint.condition()));
            c.setOperator(constraint.operator().equals(Operator.EMPTY) ? null : constraint.operator());
            c.setComparedValues(constraint.comparedValues());
            c.setExpression(StringUtils.nonEmptyValue(constraint.expression()));
            c.setMessage(constraint.message());
            c.setProfile(StringUtils.nonEmptyValue(constraint.profile()));
            if (!TypeUtils.isValueBased(field.getType())) {
                Class<?> attributeInnerType = TypeUtils.isCollection(field.getType()) ?
                        TypeUtils.getElementType(field.getType(), field) : field.getType();
                loadValidationMeta(attributeInnerType, c.getProfile());
            }
            criteria.add(c);
        }
        AttributeValidationMetadata av = new AttributeValidationMetadata();
        av.setName(field.getName());
        av.setJavaType(field.getType().isArray() ? List.class : field.getType());
        av.setCriteria(criteria);
        return av;
    }
}
