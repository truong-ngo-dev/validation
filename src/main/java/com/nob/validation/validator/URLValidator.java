package com.nob.validation.validator;


import com.nob.utils.StringUtils;
import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;
import com.nob.validation.validator.constant.ParamKey;

import java.util.List;
import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#URL}
 * <p>
 * Apply for string type
 * @author Truong Ngo
 * */
public class URLValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        String s = (String) attribute.getValue();
        if (Objects.isNull(s)) return Result.valid();
        @SuppressWarnings("unchecked")
        List<String> schemes = (List<String>) constraint.getParam(ParamKey.SCHEMES);
        boolean valid = StringUtils.isUrl(s, schemes);
        return valid ? Result.valid() : Result.invalid(String.format(Message.URL, String.join(",", schemes)));
    }
}
