package com.nob.validation.validation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nob.validation.metadata.validation.ValidationCriteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationResult {

    private static final EvaluationResult SUCCESS = new EvaluationResult(true, null, null);

    private final boolean valid;
    private Object message;

    @JsonIgnore
    private ValidationCriteria criteria;


    public EvaluationResult(boolean valid, Object message, ValidationCriteria criteria) {
        this.valid = valid;
        this.message = message;
        this.criteria = criteria;
    }

    public static EvaluationResult success() {
        return SUCCESS;
    }

    public static EvaluationResult failure(Object message, ValidationCriteria criteria) {
        return new EvaluationResult(false, message, criteria);
    }
}
