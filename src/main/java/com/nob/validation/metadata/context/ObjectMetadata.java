package com.nob.validation.metadata.context;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Describe the root validation context
 * */
@Getter
@Setter
public class ObjectMetadata extends AbstractContextMetadata {

    /**
     * Contains object's attribute metadata
     * */
    private List<AttributeMetadata> attributeMetadata;

    public ObjectMetadata(String name, Class<?> javaType, String type) {
        super(name, javaType, type);
    }

}
