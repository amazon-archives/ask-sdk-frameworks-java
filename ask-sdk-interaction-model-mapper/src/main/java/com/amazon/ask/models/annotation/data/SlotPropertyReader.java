package com.amazon.ask.models.annotation.data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD, TYPE})
public @interface SlotPropertyReader {
    Class<? extends com.amazon.ask.models.mapper.slot.SlotPropertyReader> value();
}
