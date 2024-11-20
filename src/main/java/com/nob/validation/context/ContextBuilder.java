package com.nob.validation.context;

import com.nob.utils.TypeUtils;
import com.nob.validation.annotation.Constraint;
import com.nob.validation.exception.ValidationMetadataException;
import com.nob.validation.metadata.context.AttributeMetadata;
import com.nob.validation.metadata.context.ContextMetadataProvider;
import com.nob.validation.metadata.context.ObjectMetadata;
import com.nob.validation.metadata.validation.AttributeValidationMetadata;
import com.nob.validation.metadata.validation.ObjectValidationMetadata;
import com.nob.validation.metadata.validation.ValidationMetadataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ContextBuilder {

    /**
     * Provider for context metadata
     * */
    private final ContextMetadataProvider metadataProvider;

    /**
     * Provider for validation meta
     * */
    private final ValidationMetadataProvider validationMetadataProvider;

    /**
     * Build validation context base on given object
     * @param o given object
     * @return {@code DefaultContext} the context represent the validated object
     * */
    public DefaultContext build(Object o, ObjectValidationMetadata validationMeta) {
        ObjectMetadata metadata = (ObjectMetadata) metadataProvider.load(o.getClass());
        DefaultContext context = new DefaultContext(metadata);
        List<String> validationAttribute = validationMeta.getProperties()
                .stream()
                .map(AttributeValidationMetadata::getName)
                .toList();
        List<Attribute> attributes = metadata.getAttributeMetadata()
                .stream()
                .filter(a -> validationAttribute.contains(a.getName()))
                .map(m -> buildAttribute(m, o))
                .toList();
        context.setValue(o);
        context.setAttributes(attributes);
        return context;
    }

    /**
     * Utility method for {@link #build(Object, ObjectValidationMetadata)}
     * <p>
     * Build the attribute context represent object property
     * @param metadata attribute metadata
     * @param o validated object
     * @return {@code Attribute} represent validated object's property
     * */
    private Attribute buildAttribute(AttributeMetadata metadata, Object o) {
        if (TypeUtils.isValueBased(metadata.getJavaType()))
            return buildValueBasedAttribute(metadata, o);
        if (TypeUtils.isCollection(metadata.getJavaType()))
            return buildCollectionAttribute(metadata, o);
        return buildObjectAttribute(metadata, o);
    }

    /**
     * Utility method for {@link #buildAttribute(AttributeMetadata, Object)}
     * <p>
     * Build the literal type attribute context
     * */
    private Attribute buildValueBasedAttribute(AttributeMetadata metadata, Object o) {
        ValueBasedAttribute attribute = new ValueBasedAttribute(metadata);
        setAttributeValue(attribute, o);
        return attribute;
    }

    /**
     * Utility method for {@link #buildAttribute(AttributeMetadata, Object)}
     * <p>
     * Build the object type attribute context
     * */
    private Attribute buildObjectAttribute(AttributeMetadata metadata, Object o) {
        ObjectTypeAttribute attribute = new ObjectTypeAttribute(metadata);
        setAttributeValue(attribute, o);
        if (Objects.isNull(attribute.getValue())) return attribute;
        Constraint[] annotations = metadata.getField().getAnnotationsByType(Constraint.class);
        @SuppressWarnings("all")
        String profile = Stream.of(annotations).map(Constraint::profile).filter(p -> !p.isEmpty()).findFirst().get();
        ObjectValidationMetadata meta = validationMetadataProvider.load(o.getClass(), profile);
        DefaultContext attributeContext = build(attribute.getValue(), meta);
        attribute.setContext(attributeContext);
        return attribute;
    }

    /**
     * Utility method for {@link #buildAttribute(AttributeMetadata, Object)}
     * <p>
     * Build the collection type attribute context
     * */
    private Attribute buildCollectionAttribute(AttributeMetadata metadata, Object o) {
        CollectionTypeAttribute attribute = new CollectionTypeAttribute(metadata);
        setAttributeValue(attribute, o);
        if (Objects.isNull(attribute.getValue())) return attribute;
        if (attribute.getValue().isEmpty()) return attribute;
        List<?> items = attribute.getValue();
        List<DefaultContext> contexts = new ArrayList<>();
        for (Object item : items) {
            if (Objects.isNull(item)) continue;
            Constraint[] annotations = metadata.getField().getAnnotationsByType(Constraint.class);
            @SuppressWarnings("all")
            String profile = Stream.of(annotations).map(Constraint::profile).filter(p -> !p.isEmpty()).findFirst().get();
            ObjectValidationMetadata meta = validationMetadataProvider.load(metadata.getElementJavaType(), profile);
            DefaultContext attributeContext = build(item, meta);
            contexts.add(attributeContext);
        }
        attribute.setContexts(contexts);
        return attribute;
    }

    /**
     * Utility method to set value for attribute context
     * @param attribute attribute context
     * @param o validated object
     * */
    private void setAttributeValue(Attribute attribute, Object o) {
        try {
            attribute.setValue(attribute.getField().get(o));
        } catch (IllegalAccessException e) {
            throw new ValidationMetadataException("Unable to set up attribute: " + attribute.getName(), e);
        }
    }
}
