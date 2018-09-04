package com.amazon.ask.interaction.annotation.type;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Labels an entity as built-in (provided by the platform)
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface BuiltIn {
}
