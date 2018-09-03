package com.amazon.ask.mvc.annotation.argument;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * If added to a controller parameter of type {@link java.util.Map}, a Map<String, String>
 * containing the slot names and their values will be passed in.
 */
@Target(value = {PARAMETER})
@Retention(value = RUNTIME)
@Documented
public @interface SlotValues {
}
