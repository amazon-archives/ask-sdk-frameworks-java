/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
     * @return name of the intent - defaults to the annotated type's {@link Class#getSimpleName()}
     */
    String value() default "";

    /**
     * @return id of the intent.
     */
    String id() default "";
}
