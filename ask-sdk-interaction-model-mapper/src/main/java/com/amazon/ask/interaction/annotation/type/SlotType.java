package com.amazon.ask.interaction.annotation.type;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Tags a class up as a slot type.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SlotType {
    /**
     * Slot type name - defaults to the annotated type's {@link Class#getSimpleName()}
     */
    String value() default "";

    /**
     * Id of the slot type.
     */
    String id() default "";
}
