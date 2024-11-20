package com.nob.validation.enumerate;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Operator applied on value based constraint
 * @author Truong Ngo
 * */
@Getter
public enum Operator {

    EMPTY(null, null, null),
    EQ("=", "eq", "equal"),
    NEQ("<>", "!eq", "not_equal"),
    GT(">", "gt", "greater"),
    LT("<", "lt", "lesser"),
    GTE(">=",  "gte", "greater_than_equal"),
    LTE("<=", "lte", "lesser_than_equal"),
    IN("IN", "in", "in"),
    NOT_IN("NOT IN", "!in", "not_in"),
    LIKE("LIKE", "like", "like"),
    NOT_LIKE("NOT LIKE", "!like", "not_like"),
    BETWEEN("BETWEEN", "bw", "between"),
    NOT_BETWEEN("NOT BETWEEN", "!bw", "not_between"),
    IS_NULL("NULL", "is", "is_null"),
    NOT_NULL("IS NOT", "!is", "not_null"),
    HAS_SIZE("SIZE", "size", "has_size"),
    MIN_SIZE("MIN_SIZE", "min_size", "min_size"),
    MAX_SIZE("MAX_SIZE", "max_size", "max_size"),
    HAS_PATTERN("HAS_PATTERN", "has_pattern", "has_pattern");

    private final String token;
    private final String shortHand;
    private final String name;

    Operator(String token,  String shortHand, String name) {
        this.token = token;
        this.shortHand = shortHand;
        this.name = name;
    }

    public static Operator ofToken(String token) {
        return Stream.of(Operator.values())
                .filter(op -> op.token.equals(token))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid token: " + token));
    }

    public static Operator ofShortHand(String shortHand) {
        return Stream.of(Operator.values())
                .filter(op -> op.shortHand.equals(shortHand))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid short hand: " + shortHand));
    }

    public static Operator ofName(String name) {
        return Stream.of(Operator.values())
                .filter(op -> op.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid name: " + name));
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
