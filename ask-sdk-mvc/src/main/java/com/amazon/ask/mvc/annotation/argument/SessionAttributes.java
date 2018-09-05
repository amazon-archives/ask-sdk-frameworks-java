/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.annotation.argument;

import com.amazon.ask.mvc.annotation.plugin.AutoArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * If added to a controller parameter of type {@link java.util.Map}, the Map
 * containing the session attributes and their values will be passed in.
 */
@Target(value = {PARAMETER})
@Retention(value = RUNTIME)
@Documented
@AutoArgumentResolver(SessionAttributes.Plugin.class)
public @interface SessionAttributes {
    class Plugin implements AutoArgumentResolver.Plugin<SessionAttributes> {
        @Override
        public Object apply(ArgumentResolverContext input, SessionAttributes sessionAttributes) {
            return input
                .getHandlerInput()
                .getRequestEnvelope()
                .getSession()
                .getAttributes();
        }
    }
}
