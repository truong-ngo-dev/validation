package com.nob.validation.context;

/**
 * Validation context
 * @author Truong Ngo
 * */
public interface Context {

    /**
     * Object name
     * */
    String getName();

    /**
     * Object java type
     * */
    Class<?> getJavaType();

    /**
     * Object user-defined type
     * */
    String getType();

    /**
     * Original context value
     * */
    Object getValue();

    /**
     * Set original value for context
     * */
    void setValue(Object value);
}
