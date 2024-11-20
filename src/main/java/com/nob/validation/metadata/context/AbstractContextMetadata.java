package com.nob.validation.metadata.context;

import lombok.Getter;

/**
 * Hold the common property of context metadata
 */
@Getter
public abstract class AbstractContextMetadata implements ContextMetadata {

    /**
     * Context element name
     * */
    private final String name;

    /**
     * Java type
     * */
    private final Class<?> javaType;

    /**
     * User-defined type
     * */
    private final String type;

    protected AbstractContextMetadata(String name, Class<?> javaType, String type) {
        this.name = name;
        this.javaType = javaType;
        this.type = type;
    }

    protected AbstractContextMetadata(String name, Class<?> javaType) {
        this.name = name;
        this.javaType = javaType;
        this.type = null;
    }
}

