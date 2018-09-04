package com.amazon.ask.interaction.annotation.data;

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
    Class<? extends com.amazon.ask.interaction.mapper.intent.IntentPropertyReader> value();
}
