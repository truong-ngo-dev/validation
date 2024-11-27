package com.nob.validation.context;

import com.nob.validation.metadata.context.AttributeMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectTypeAttribute extends AbstractAttribute implements Attribute {

    private ObjectBasedContext context;

    public ObjectTypeAttribute(AttributeMetadata attributeMetadata) {
        super(attributeMetadata);
    }
}
