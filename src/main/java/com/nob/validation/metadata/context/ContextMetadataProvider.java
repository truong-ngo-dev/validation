package com.nob.validation.metadata.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Context metadata store / provider
 * */
@Slf4j
@Component
public class ContextMetadataProvider {

    /**
     * Hold the all the validation context metadata generated when application start
     * */
    private final Map<Class<?>, ContextMetadata> contexts = new HashMap<>();

    /**
     * Add context metadata
     * @param contextMetadata the validation context
     * */
    public void add(final ContextMetadata contextMetadata) {
        if (!contexts.containsKey(contextMetadata.getJavaType())) {
            contexts.put(contextMetadata.getJavaType(), contextMetadata);
        }
    }

    /**
     * Load context metadata
     * @param javaType the context java type
     * @return the corresponding context or null if not found
     * */
    public ContextMetadata load(final Class<?> javaType) {
        if (!contexts.containsKey(javaType)) {
            log.info("No context found for type {}", javaType);
            return null;
        }
        return contexts.get(javaType);
    }

    /**
     * Check if given context metadata type is already loaded
     * @param javaType given context java type
     * @return true if context is already loaded otherwise false
     * */
    public boolean contains(final Class<?> javaType) {
        return contexts.containsKey(javaType);
    }

}
