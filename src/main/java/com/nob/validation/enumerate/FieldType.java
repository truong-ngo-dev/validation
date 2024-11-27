package com.nob.validation.enumerate;

import com.nob.utils.TypeUtils;

import java.util.Objects;

/**
 * User defined type for property
 * */
public enum FieldType {
    STRING,
    INTEGER,
    DECIMAL,
    BOOLEAN,
    DATE,
    OBJECT,
    COLLECTION;

    public static final FieldType[] ALL = FieldType.values();

    public static FieldType[] of(FieldType... types) {
        return types;
    }

    public static FieldType of(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        if (TypeUtils.isString(clazz)) return STRING;
        if (TypeUtils.isInteger(clazz)) return INTEGER;
        if (TypeUtils.isDecimal(clazz)) return DECIMAL;
        if (TypeUtils.isBoolean(clazz)) return BOOLEAN;
        if (TypeUtils.isDate(clazz)) return DATE;
        if (TypeUtils.isCollection(clazz)) return COLLECTION;
        return OBJECT;
    }
}
