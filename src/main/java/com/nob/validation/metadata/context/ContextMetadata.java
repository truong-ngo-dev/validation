package com.nob.validation.metadata.context;

/**
 * Metadata that describe validation context
 * */
public interface ContextMetadata {

    /**
     * Name of the object represented by context
     * */
    String getName();

    /**
     * Java type of object
     * */
    Class<?> getJavaType();

    /**
     * User-defined type of object
     * */
    String getType();

}
