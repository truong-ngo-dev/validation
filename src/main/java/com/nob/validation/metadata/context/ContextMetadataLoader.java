package com.nob.validation.metadata.context;

import com.nob.utils.TypeUtils;
import com.nob.validation.exception.ValidationMetadataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Load the validation context metadata when application start
 * */
@Component
@RequiredArgsConstructor
public class ContextMetadataLoader {

    /**
     * Context metadata provider
     * */
    private final ContextMetadataProvider contextMetadataProvider;

    /**
     * Load context metadata when application start
     * @param javaType object java type
     * @param name object name
     * */
    public void loadMetadata(Class<?> javaType, String name) {
        if (contextMetadataProvider.contains(javaType)) return;
        ObjectMetadata context = new ObjectMetadata(name, javaType, null);
        contextMetadataProvider.add(context);
        List<AttributeMetadata> attributesMetadata = new ArrayList<>();
        Field[] fields = javaType.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            AttributeMetadata attributeMetadata = loadAttribute(javaType, field.getType(), field.getName());
            attributesMetadata.add(attributeMetadata);
        }
        context.setAttributeMetadata(attributesMetadata);
    }

    /**
     * Load context attribute
     * @param objJavaType outer object java type
     * @param javaType attribute java type
     * @param name attribute name
     * */
    public AttributeMetadata loadAttribute(Class<?> objJavaType, Class<?> javaType, String name) {

        if (TypeUtils.isValueBased(javaType)) {
            return new AttributeMetadata(objJavaType, name, javaType, null);
        }

        if (TypeUtils.isCollection(javaType)) {
            Field field;
            try {
                field = objJavaType.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                throw new ValidationMetadataException("Unable to attribute metadata");
            }
            Class<?> elementType = TypeUtils.getElementType(javaType, field);
            if (!contextMetadataProvider.contains(elementType)) loadMetadata(elementType, null);
            Class<?> collectionType = javaType.isArray() ? List.class : javaType;
            return new AttributeMetadata(objJavaType, name, collectionType, null, elementType, null);
        }

        if (!contextMetadataProvider.contains(javaType)) loadMetadata(javaType, name);
        return new AttributeMetadata(objJavaType, name, javaType, null);
    }
}
