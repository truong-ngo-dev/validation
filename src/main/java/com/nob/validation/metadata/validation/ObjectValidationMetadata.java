package com.nob.validation.metadata.validation;

import lombok.Data;

import java.util.List;

/**
 * Validation metadata for object
 * */
@Data
public class ObjectValidationMetadata implements ValidationMetadata {

    /**
     * Profile version of object validation
     * */
    private String profile;

    /**
     * Object java type
     * */
    private Class<?> javaType;

    /**
     * Attribute validation metadata
     * */
    private List<AttributeValidationMetadata> properties;
}
