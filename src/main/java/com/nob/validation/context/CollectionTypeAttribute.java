package com.nob.validation.context;

import com.nob.validation.metadata.context.AttributeMetadata;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CollectionTypeAttribute extends AbstractAttribute implements Attribute {

    /**
     * Hold the collection of element's context
     * */
    private List<DefaultContext> contexts;

    /**
     * Java type of element
     * */
    private Class<?> elementJavaType;

    /**
     * Element user-defined type
     * */
    private String elementType;

    public CollectionTypeAttribute(AttributeMetadata attributeMetadata) {
        super(attributeMetadata);
        elementJavaType = attributeMetadata.getElementJavaType();
        elementType = attributeMetadata.getElementType();
    }

    @Override
    public List<?> getValue() {
        Object value = super.getValue();
        if (Objects.isNull(value)) return null;
        if (value.getClass().isArray()) {
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < Array.getLength(value); i++) {
                result.add(Array.get(value, i));
            }
            return result;
        };
        return (List<?>) value;
    }
}
