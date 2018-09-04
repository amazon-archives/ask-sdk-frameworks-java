package com.amazon.ask.mvc.mapper;

import com.amazon.ask.mvc.annotation.mapping.IntentMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Determines the priority 'level' of a method within a controller.
 *
 * For example, given controller with two methods annotated with {@link IntentMapping} for intent, "A":
 *
 * <code>
 * @IntentMapping("A")
 * public Response handle1(..) {}
 *
 *
 * @IntentMapping("A")
 * @WhenSessionAttribute(..)
 * public Response handle2(..) {}
 * </code>
 *
 * `handle1` and `handle2` both accept intent "A", but `handle2` has a condition. If `handle1` is
 * checked before `handle2`, then `handle2` will never be invoked. Adding `@Priority(1)` to `handle2`
 * ensures the method is checked before `handle1`.
 *
 * Methods who are not annotated with {@link Priority} are considered to have `0` priority.
 *
 * Methods run in descending order (largest number first). So:
 * - priority > 0: it runs before default
 * - priority = 0: it runs at the same time as default
 * - priority < 0: it runs after default methods
 *
 * Use of {@link Priority} is discouraged as it could be difficult to maintain. Predicates should
 * attempt to be mutually exclusive.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Priority {
    int MAXIMUM = Integer.MAX_VALUE;
    int MINIMUM = Integer.MIN_VALUE;
    int HIGH = 10;
    int DEFAULT = 0;
    int LOW = -10;

    int value() default DEFAULT;
}
