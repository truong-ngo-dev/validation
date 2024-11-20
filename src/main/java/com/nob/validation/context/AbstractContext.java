package com.nob.validation.context;

import com.nob.validation.metadata.context.ContextMetadata;
import lombok.Getter;
import lombok.Setter;

/**
 * Common abstraction for all context
 * @author Truong Ngo
 * */
@Setter
@Getter
public abstract class AbstractContext implements Context {

    private final String name;
    private final Class<?> javaType;
    private final String type;
    private Object value;

    protected AbstractContext(ContextMetadata metadata) {
        this.name = metadata.getName();
        this.javaType = metadata.getJavaType();
        this.type = metadata.getType();
    }
}
