package com.nob.validation.constraint;

import lombok.Data;

import java.util.Map;

/**
 * Constraint apply for object properties
 * */
@Data
public class Constraint {

    /**
     * Constraint type
     * */
    private ConstraintType type;


    /**
     * Applied condition for constraint
     * */
    private String condition;


    /**
     * Parameter of constraint
     * */
    private Map<String, Object> params;


    public Constraint(ConstraintType type, String condition, Map<String, Object> params) {
        this.type = type;
        this.condition = condition;
        this.params = params;
    }

    /**
     * Get the specific parameter of constraint
     * @param key parameter key
     * @param clazz desire type
     * @param <T> param type
     * @return value of key
     * */
    public <T> T getParam(String key, Class<T> clazz) {
        return clazz.cast(params.get(key));
    }


    /**
     * Get the specific parameter of constraint
     * @param key parameter key
     * @return value of key
     * */
    public Object getParam(String key) {
        return params.get(key);
    }

}
