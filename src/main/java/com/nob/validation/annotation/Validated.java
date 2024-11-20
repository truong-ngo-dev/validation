package com.nob.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation on method indicate that validation will be applied on that it's parameter. Eg:
 * <blockquote><pre>
 *     {@code @Validated}
 *     public ResponseEntity<Model> someMethod(@Valid(profile = "aProfile") Parameter parameter) {...}
 * </pre></blockquote>
 * @see Valid
 * @author Truong Ngo
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validated {
}
