package com.nob.validation.constraint;

/**
 * Type of constraint
 * @author Truong Ngo
 * */
public enum ConstraintType {

    // All type

    /**
     * Required type. Eg:
     * <blockquote><pre>
     *     {"type": "REQUIRED"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.RequiredValidator
     * */
    REQUIRED,

    /**
     * Null type. Eg:
     * <blockquote><pre>
     *     {"type": "NULL"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.NullValidator
     * */
    NULL,

    /**
     * Expression type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "EXPRESSION",
     *         "params": {
     *             "expression": "name.length == 10"
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.ExpressionValidator
     * */
    EXPRESSION,

    // Collection and string type

    /**
     * Size type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "SIZE",
     *         "params": {
     *             "min": 10,  // one of min or max can be absent but not both
     *             "max": 20
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.SizeValidator
     * */
    SIZE,

    // Collection & object type

    /**
     * Not empty type. Eg:
     * <blockquote><pre>
     *     {"type": "NOT_EMPTY"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.NotEmptyValidator
     * */
    NOT_EMPTY,

    /**
     * Collection attribute type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "COLLECTION_ATTRIBUTE",
     *         "params": {
     *             "profile": "default" // profile of element type
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.CollectionAttributeValidator
     * */
    COLLECTION_ATTRIBUTE,

    /**
     * Object attribute type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "OBJECT_ATTRIBUTE",
     *         "params": {
     *             "profile": "default"
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.ObjectAttributeValidator
     * */
    OBJECT_ATTRIBUTE,

    // Number type

    /**
     * Min max type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "MIN_MAX",
     *         "params": {
     *             "min": 10,  // one of min or max can be absent but not both
     *             "max": 20
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.MinMaxValidator
     * */
    MIN_MAX,

    /**
     * Decimal min max type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "DECIMAL_MIN_MAX",
     *         "params": {
     *             "min": "10.5",  // one of min or max can be absent but not both
     *             "max": "20.5"   // string is use for precision
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.DecimalMinMaxValidator
     * */
    DECIMAL_MIN_MAX,

    // Boolean type

    /**
     * Assert true type. Eg:
     * <blockquote><pre>
     *     {"type": "ASSERT_TRUE"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.AssertTrueValidator
     * */
    ASSERT_TRUE,

    /**
     * Assert false type. Eg:
     * <blockquote><pre>
     *     {"type": "ASSERT_FALSE"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.AssertFalseValidator
     * */
    ASSERT_FALSE,

    // String type

    /**
     * Email type. Eg:
     * <blockquote><pre>
     *     {"type": "EMAIL"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.EmailValidator
     * */
    EMAIL,

    /**
     * Not blank type. Eg:
     * <blockquote><pre>
     *     {"type": "NOT_BLANK"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.NotBlankValidator
     * */
    NOT_BLANK,

    /**
     *  Url type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "URL",
     *         "params": {
     *             "schemes": ["http", "https", "ftp"]
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.URLValidator
     * */
    URL,

    /**
     *  Pattern type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "PATTERN",
     *         "params": {
     *             "schemes": ["http", "https", "ftp"]
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.PatternValidator
     * */
    PATTERN,

    /**
     * Alphanumeric type. Eg:
     * <blockquote><pre>
     *     {"type": "ALPHANUMERIC"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.AlphanumericValidator
     * */
    ALPHANUMERIC,

    /**
     * Not allowed character type. Eg:
     * <blockquote><pre>
     *     {
     *         "type": "NOT_ALLOWED_CHARACTERS",
     *         "params": {
     *             "characters": ["s", "a", "f"]
     *         }
     *     }
     * </pre></blockquote>
     * @see com.nob.validation.validator.NotAllowedCharactersValidator
     * */
    NOT_ALLOWED_CHARACTERS,

    // Date type

    /**
     * Future type. Eg:
     * <blockquote><pre>
     *     {"type": "FUTURE"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.FutureValidator
     * */
    FUTURE,

    /**
     * Future or present type. Eg:
     * <blockquote><pre>
     *     {"type": "FUTURE_OR_PRESENT"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.FutureOrPresentValidator
     * */
    FUTURE_OR_PRESENT,

    /**
     * Past type. Eg:
     * <blockquote><pre>
     *     {"type": "PAST"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.PastValidator
     * */
    PAST,

    /**
     * Past or present type. Eg:
     * <blockquote><pre>
     *     {"type": "PAST_OR_PRESENT"}
     * </pre></blockquote>
     * @see com.nob.validation.validator.PastOrPresentValidator
     * */
    PAST_OR_PRESENT,
}
