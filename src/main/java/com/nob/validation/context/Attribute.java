package com.nob.validation.context;

import java.lang.reflect.Field;

/**
 * Context's attribute
 * @author Truong Ngo
 * */
public interface Attribute extends Context {

    /**
     * Attribute reflect object
     * */
    Field getField();

}
