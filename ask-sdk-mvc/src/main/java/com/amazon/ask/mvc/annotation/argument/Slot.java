package com.amazon.ask.mvc.annotation.argument;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * If this annotation is added to a parameter of type {@link com.amazon.ask.model.Slot}, the corresponding
 * slot will be passed in, if it can be resolved by name from the Intent Request. It can also be added to
 * parameters of type String, in which case the value of the slot will be passed in.
 */
@Target(value = {PARAMETER})
@Retention(value = RUNTIME)
@Documented
public @interface Slot {
    String value(); //the slot name
}
