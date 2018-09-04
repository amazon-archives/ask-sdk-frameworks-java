package com.amazon.ask.interaction.annotation.type;

import com.amazon.ask.interaction.mapper.intent.IntentReader;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Tags a class up as an intent, providing a name and an optional custom {@link IntentReader}
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Intent {
    /**
     * Name of the intent - defaults to the annotated type's {@link Class#getSimpleName()}
     */
    String value() default "";

    /**
     * Id of the intent.
     */
    String id() default "";
}
