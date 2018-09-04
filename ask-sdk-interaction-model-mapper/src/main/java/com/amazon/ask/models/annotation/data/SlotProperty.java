package com.amazon.ask.models.annotation.data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface SlotProperty {
    String name() default "";

    Class<?> type() default Object.class;
}
