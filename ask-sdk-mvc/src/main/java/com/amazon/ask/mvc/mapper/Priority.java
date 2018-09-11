/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
 * <pre>
 * &#64;IntentMapping("A")
 * public Response handle1(..) {}
 *
 *
 * &#64;IntentMapping("A")
 * &#64;WhenSessionAttribute(..)
 * public Response handle2(..) {}
 * </pre>
 *
 * `handle1` and `handle2` both accept intent "A", but `handle2` has a condition. If `handle1` is
 * checked before `handle2`, then `handle2` will never be invoked. Adding `@Priority(1)` to `handle2`
 * ensures the method is checked before `handle1`.
 *
 * Methods who are not annotated with {@link Priority} are considered to have `0` priority.
 *
 * Methods run in descending order (largest number first). So:
 * - priority {@literal >} 0: it runs before default
 * - priority = 0: it runs at the same time as default
 * - priority {@literal <} 0: it runs after default methods
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
