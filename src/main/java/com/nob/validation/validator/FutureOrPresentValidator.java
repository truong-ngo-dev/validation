package com.nob.validation.validator;

import com.nob.validation.constraint.Constraint;
import com.nob.validation.context.Attribute;
import com.nob.validation.context.Context;
import com.nob.validation.validator.constant.Message;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Validator for constraint of type {@link com.nob.validation.constraint.ConstraintType#FUTURE_OR_PRESENT}
 * <p>
 * Apply for {@code Date} type
 * @author Truong Ngo
 * */
public class FutureOrPresentValidator implements Validator {

    @Override
    public Result validateInternal(Context context, Attribute attribute, Constraint constraint) {
        Object val = attribute.getValue();
        if (Objects.isNull(val)) return Result.valid();
        if (val instanceof Date date) return !date.before(new Date()) ? Result.valid() : Result.invalid(Message.FUTURE);
        if (val instanceof LocalDate localDate) return !localDate.isBefore(LocalDate.now()) ? Result.valid() : Result.invalid(Message.FUTURE);
        if (val instanceof LocalDateTime localDateTime) return !localDateTime.isBefore(LocalDateTime.now()) ? Result.valid() : Result.invalid(Message.FUTURE);
        if (val instanceof ZonedDateTime zonedDateTime) return !zonedDateTime.isBefore(ZonedDateTime.now()) ? Result.valid() : Result.invalid(Message.FUTURE);
        if (val instanceof Instant instant) return !instant.isBefore(Instant.now()) ? Result.valid() : Result.invalid(Message.FUTURE);
        throw new IllegalArgumentException("Property not a date type!");
    }
}
