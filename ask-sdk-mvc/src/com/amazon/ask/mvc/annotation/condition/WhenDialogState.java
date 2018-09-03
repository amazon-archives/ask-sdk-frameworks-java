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
     * Allowed dialog state values.
     */
    DialogState[] value() default {};

    /**
     * Allowed dialog state values.
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