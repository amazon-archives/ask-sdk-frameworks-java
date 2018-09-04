package com.amazon.ask.mvc.annotation.plugin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.models.Utils;
import com.amazon.ask.mvc.annotation.condition.WhenDialogState;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.mapper.AnnotationContext;
import com.amazon.ask.mvc.plugin.PredicateResolver;

import java.lang.annotation.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Added to annotations that make a mapping conditional, based on a predicate.
 *
 * @see WhenSessionAttribute
 * @see WhenDialogState
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoPredicate {
    Class<? extends Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<AnnotationContext, A, Predicate<HandlerInput>> {
    }

    class Scanner implements PredicateResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Optional<Predicate<HandlerInput>> resolve(AnnotationContext context) {
            return context.scanAnnotations()
                .flatMap(annotation -> {
                    AutoPredicate meta = annotation.annotationType().getAnnotation(AutoPredicate.class);
                    if (meta != null) {
                        AutoPredicate.Plugin<Annotation> plugin = (AutoPredicate.Plugin<Annotation>) Utils.instantiate(meta.value());
                        return Stream.of(plugin.apply(context, annotation));
                    } else {
                        return Stream.empty();
                    }
                })
                .reduce(Predicate::and);
        }
    }
}
