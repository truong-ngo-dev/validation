package com.nob.validation.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nob.validation.metadata.context.AttributeMetadata;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * Common abstract attribute
 * @author Truong Ngo
 * */
@Getter
@Setter
public abstract class AbstractAttribute extends AbstractContext implements Attribute {

    /**
     * Reflection element of attribute
     * */
    @JsonIgnore
    private Field field;

    protected AbstractAttribute(AttributeMetadata attributeMetadata) {
        super(attributeMetadata);
        field = attributeMetadata.getField();
    }

}
