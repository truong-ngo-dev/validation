package com.nob.validation.metadata.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nob.validation.exception.ValidationMetadataException;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * Describe the attribute of the validation context
 * */
@Setter
@Getter
public class AttributeMetadata extends AbstractContextMetadata {

    /**
     * Reflect field represent attribute
     * */
    @JsonIgnore
    private Field field;

    /**
     * Element java type of case collection
     * */
    private Class<?> elementJavaType;

    /**
     * User-defined type of element in collection
     * */
    private String elementType;

    /**
     * For non-collection attribute
     * */
    public AttributeMetadata(Class<?> objJavaType, String name, Class<?> javaType, String type) {
        super(name, javaType, type);
        try {
            field = objJavaType.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ValidationMetadataException("Unable to setup field name: " + name, e);
        }
    }

    /**
     * For collection attribute
     * */
    public AttributeMetadata(Class<?> objJavaType, String name, Class<?> javaType, String type, Class<?> elementJavaType, String elementType) {
        this(objJavaType, name, javaType, type);
        this.elementJavaType = elementJavaType;
        this.elementType = elementType;
    }
}
