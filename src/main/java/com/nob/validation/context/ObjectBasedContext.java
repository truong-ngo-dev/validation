package com.nob.validation.context;

import com.nob.validation.metadata.context.ContextMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Default implementation of context
 * */
@Getter
@Setter
public class ObjectBasedContext extends AbstractContext {

    private List<Attribute> attributes;

    public ObjectBasedContext(ContextMetadata metadata) {
        super(metadata);
    }

    public Attribute getAttribute(String name) {
        return attributes.stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
