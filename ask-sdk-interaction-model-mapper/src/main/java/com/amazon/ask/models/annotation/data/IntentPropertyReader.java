package com.amazon.ask.models.annotation.data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD, TYPE})
public @interface IntentPropertyReader {
    Class<? extends com.amazon.ask.models.mapper.intent.IntentPropertyReader> value();
}
