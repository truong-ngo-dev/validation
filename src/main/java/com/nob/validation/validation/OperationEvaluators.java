package com.nob.validation.validation;

import com.nob.utils.DateUtils;
import com.nob.utils.TypeUtils;
import com.nob.validation.enumerate.Operator;
import com.nob.validation.metadata.validation.ValidationCriteria;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Evaluator for operation of criteria
 * */
public class OperationEvaluators {

    /**
     * Utility class
     * */
    private OperationEvaluators() {
        throw new UnsupportedOperationException("Cannot instantiate utility class: " + this.getClass().getName());
    }

    /**
     * Resolve criteria base on operator and compared value
     * @param value value
     * @param criteria criteria for value
     * */
    public static boolean evaluate(Object value, ValidationCriteria criteria) {
        Operator operator = criteria.getOperator();
        return switch (operator) {
            case EQ -> equalEvaluator.evaluate(value, criteria);
            case NEQ -> notEqualEvaluator.evaluate(value, criteria);
            case GT -> greaterThanEvaluator.evaluate(value, criteria);
            case GTE -> greaterThanEqualEvaluator.evaluate(value, criteria);
            case LT -> lesserThanEvaluator.evaluate(value, criteria);
            case LTE -> lesserThanEqualEvaluator.evaluate(value, criteria);
            case IN -> inEvaluator.evaluate(value, criteria);
            case NOT_IN -> notInEvaluator.evaluate(value, criteria);
            case BETWEEN -> betweenEvaluator.evaluate(value, criteria);
            case NOT_BETWEEN -> notBetweenEvaluator.evaluate(value, criteria);
            case IS_NULL -> isNullEvaluator.evaluate(value, criteria);
            case NOT_NULL -> notNullEvaluator.evaluate(value, criteria);
            case LIKE -> likeEvaluator.evaluate(value, criteria);
            case NOT_LIKE -> notLikeEvaluator.evaluate(value, criteria);
            case HAS_SIZE -> hasLengthEvaluator.evaluate(value, criteria);
            case MIN_SIZE -> minLengthEvaluator.evaluate(value, criteria);
            case MAX_SIZE -> maxLengthEvaluator.evaluate(value, criteria);
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }

    static IllegalArgumentException unsupportedOperator(Operator operator, Class<?> clazz) {
        throw new IllegalArgumentException("Unsupported operation: " + operator.getName() + " for type: " + clazz.getName());
    }

    @FunctionalInterface
    interface Evaluator {
        boolean evaluate(Object value, ValidationCriteria criteria);
    }

    static Evaluator equalEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            Number c = NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass());
            return n.equals(c);
        }
        if (v instanceof String s) {
            return s.equals(cr.getComparedValues()[0]);
        }
        if (TypeUtils.isDate(v.getClass())) {
            Object c = DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return c.equals(v);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator notEqualEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            Number c = NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass());
            return !n.equals(c);
        }
        if (v instanceof String s) {
            return !s.equals(cr.getComparedValues()[0]);
        }
        if (TypeUtils.isDate(v.getClass())) {
            Object c = DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return !c.equals(v);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator greaterThanEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            Number c = NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass());
            return com.nob.utils.NumberUtils.compare(n, c) > 0;
        }
        if (TypeUtils.isDate(v.getClass())) {
            Object comparedDate = DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return DateUtils.compare(v, comparedDate) > 0;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator greaterThanEqualEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            Number c = NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass());
            return com.nob.utils.NumberUtils.compare(n, c) >= 0;
        }
        if (TypeUtils.isDate(v.getClass())) {
            Object comparedDate = DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return DateUtils.compare(v, comparedDate) >= 0;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator lesserThanEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            Number c = NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass());
            return com.nob.utils.NumberUtils.compare(n, c) < 0;
        }
        if (TypeUtils.isDate(v.getClass())) {
            Object comparedDate = DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return DateUtils.compare(v, comparedDate) < 0;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator lesserThanEqualEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            Number c = NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass());
            return com.nob.utils.NumberUtils.compare(n, c) <= 0;
        }
        if (TypeUtils.isDate(v.getClass())) {
            Object comparedDate = DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return DateUtils.compare(v, comparedDate) <= 0;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator inEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            List<? extends Number> cs = Stream.of(cr.getComparedValues())
                    .map(num -> NumberUtils.parseNumber(num, n.getClass()))
                    .toList();
            return cs.contains(n);
        }
        if (v instanceof String s) {
            return Arrays.asList(cr.getComparedValues()).contains(s);
        }
        if (TypeUtils.isDate(v.getClass())) {
            List<?> date = Stream.of(cr.getComparedValues())
                    .map(d -> DateUtils.parseDateWithUTCZone(d, cr.getDatePattern(), v.getClass()))
                    .toList();
                    DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return date.contains(v);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator notInEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            Number n = (Number) v;
            List<? extends Number> cs = Stream.of(cr.getComparedValues())
                    .map(num -> NumberUtils.parseNumber(num, n.getClass()))
                    .toList();
            return !cs.contains(n);
        }
        if (v instanceof String s) {
            return !Arrays.asList(cr.getComparedValues()).contains(s);
        }
        if (TypeUtils.isDate(v.getClass())) {
            List<?> date = Stream.of(cr.getComparedValues())
                    .map(d -> DateUtils.parseDateWithUTCZone(d, cr.getDatePattern(), v.getClass()))
                    .toList();
            DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass());
            return !date.contains(v);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator betweenEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            BigDecimal n = new BigDecimal(v.toString());
            BigDecimal s = new BigDecimal(NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass()).toString());
            BigDecimal e = new BigDecimal(NumberUtils.parseNumber(cr.getComparedValues()[1], n.getClass()).toString());
            return n.compareTo(s) >= 0  && n.compareTo(e) <= 0;
        }
        if (TypeUtils.isDate(v.getClass())) {
            Instant d = DateUtils.toInstantUTC(v);
            Object s = DateUtils.toInstantUTC(DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass()));
            Object e = DateUtils.toInstantUTC(DateUtils.parseDateWithUTCZone(cr.getComparedValues()[1], cr.getDatePattern(), v.getClass()));
            return DateUtils.compare(d, s) >= 0 && DateUtils.compare(d, e) <= 0;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator notBetweenEvaluator = (v, cr) -> {
        if (Number.class.isAssignableFrom(v.getClass())) {
            BigDecimal n = new BigDecimal(v.toString());
            BigDecimal s = new BigDecimal(NumberUtils.parseNumber(cr.getComparedValues()[0], n.getClass()).toString());
            BigDecimal e = new BigDecimal(NumberUtils.parseNumber(cr.getComparedValues()[1], n.getClass()).toString());
            return !(n.compareTo(s) >= 0  && n.compareTo(e) <= 0);
        }
        if (TypeUtils.isDate(v.getClass())) {
            Instant d = DateUtils.toInstantUTC(v);
            Object s = DateUtils.toInstantUTC(DateUtils.parseDateWithUTCZone(cr.getComparedValues()[0], cr.getDatePattern(), v.getClass()));
            Object e = DateUtils.toInstantUTC(DateUtils.parseDateWithUTCZone(cr.getComparedValues()[1], cr.getDatePattern(), v.getClass()));
            return !(DateUtils.compare(d, s) >= 0 && DateUtils.compare(d, e) <= 0);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator isNullEvaluator = (v, cr) -> Objects.isNull(v);

    static Evaluator notNullEvaluator = (v, cr) -> Objects.nonNull(v);

    static Evaluator likeEvaluator = (v, cr) -> {
        if (v instanceof String s) {
            String pattern = cr.getComparedValues()[0];
            return s.contains(pattern);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator notLikeEvaluator = (v, cr) -> {
        if (v instanceof String s) {
            String pattern = cr.getComparedValues()[0];
            return !s.contains(pattern);
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator hasLengthEvaluator = (v, cr) -> {
        Integer size = Integer.valueOf(cr.getComparedValues()[0]);
        if (v instanceof String s) return s.length() == size;
        if (TypeUtils.isCollection(v.getClass())) {
            Collection<?> c = TypeUtils.getCollection(v);
            assert c != null;
            return c.size() == size;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator minLengthEvaluator = (v, cr) -> {
        Integer size = Integer.valueOf(cr.getComparedValues()[0]);
        if (v instanceof String s) return s.length() >= size;
        if (TypeUtils.isCollection(v.getClass())) {
            Collection<?> c = TypeUtils.getCollection(v);
            assert c != null;
            return c.size() >= size;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };

    static Evaluator maxLengthEvaluator = (v, cr) -> {
        Integer size = Integer.valueOf(cr.getComparedValues()[0]);
        if (v instanceof String s) return s.length() <= size;
        if (TypeUtils.isCollection(v.getClass())) {
            Collection<?> c = TypeUtils.getCollection(v);
            assert c != null;
            return c.size() <= size;
        }
        throw unsupportedOperator(cr.getOperator(), v.getClass());
    };
}
