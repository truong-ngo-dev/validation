package com.nob.validation.enumerate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Collection;
import java.util.Date;
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

    public static FieldType[] of(FieldType... types) {
        return types;
    }

    public static FieldType ofValue(Object value) {
        Objects.requireNonNull(value);
        if (isString(value)) return STRING;
        if (isInteger(value)) return INTEGER;
        if (isDecimal(value)) return DECIMAL;
        if (isBoolean(value)) return BOOLEAN;
        if (isDate(value)) return DATE;
        if (isCollection(value)) return COLLECTION;
        return OBJECT;
    }

    public static boolean isString(Object o) {
        return o instanceof String || o instanceof Character;
    }

    public static boolean isInteger(Object o) {
        return o instanceof Integer || o instanceof Long || o instanceof BigInteger;
    }

    public static boolean isDecimal(Object o) {
        return o instanceof BigDecimal || o instanceof Double || o instanceof Float;
    }

    public static boolean isBoolean(Object o) {
        return o instanceof Boolean;
    }

    public static boolean isDate(Object o) {
        if (o instanceof Date) return true;
        if (o instanceof LocalDate) return true;
        if (o instanceof LocalTime) return true;
        if (o instanceof LocalDateTime) return true;
        if (o instanceof ZonedDateTime) return true;
        return o instanceof Instant;
    }

    public static boolean isCollection(Object o) {
        if (o.getClass().isArray()) return true;
        return Collection.class.isAssignableFrom(o.getClass());
    }
}
