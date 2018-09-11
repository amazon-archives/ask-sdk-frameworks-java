/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.annotation.condition;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.mvc.annotation.plugin.AutoPredicate;
import com.amazon.ask.mvc.mapper.AnnotationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * When added to a handler method, the method will only be invoked if the dialog's
 * state is in one of the provided values.
 */
@Target(value = METHOD)
@Retention(value = RUNTIME)
@AutoPredicate(WhenDialogState.Plugin.class)
public @interface WhenDialogState {
    /**
     * @return allowed dialog state values.
     */
    DialogState[] value() default {};

    /**
     * @return allowed dialog state values.
     */
    DialogState[] states() default {};

    class Plugin implements AutoPredicate.Plugin<WhenDialogState> {
        @Override
        public Predicate<HandlerInput> apply(AnnotationContext entity, WhenDialogState annotation) {
            List<DialogState> annotationValues = Arrays.asList(annotation.states().length == 0
                ? annotation.value()
                : annotation.states());

            Set<String> values = annotationValues.stream()
                .map(DialogState::toString)
                .collect(Collectors.toSet());

            return input -> {
                Request request = input.getRequestEnvelope().getRequest();

                return request instanceof IntentRequest &&
                    ((IntentRequest)request).getDialogState() != null &&
                    values.contains(((IntentRequest)request).getDialogState().toString());
            };
        }
    }
}