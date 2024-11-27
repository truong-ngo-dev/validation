package com.nob.validation.metadata.validation;

import com.nob.validation.constraint.Constraint;
import lombok.Data;

import java.util.List;

/**
 * Attribute validation metadata
 * */
@Data
public class AttributeValidationMetadata implements ValidationMetadata {

    /**
     * Attribute name
     * */
    private String name;

    /**
     * Attribute java type
     * */
    private Class<?> javaType;

    /**
     * Criteria apply on attribute
     * */
    private List<Constraint> constraints;
}
