package com.amazon.ask.mvc.annotation.plugin;

import com.amazon.ask.interaction.Utils;
import com.amazon.ask.mvc.annotation.condition.WhenDialogState;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.lang.annotation.*;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Added to annotations that make a mapping conditional, based on a predicate.
 *
 * @see WhenSessionAttribute
 * @see WhenDialogState
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoArgumentResolver {
    Class<? extends Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<ArgumentResolverContext, A, Object> {}

    class Scanner implements ArgumentResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Optional<Object> resolve(ArgumentResolverContext input) {
            for (Annotation annotation : input.getMethodParameter().getAnnotations()) {
                AutoArgumentResolver meta = annotation.annotationType().getAnnotation(AutoArgumentResolver.class);
                if (meta != null) {
                    AutoArgumentResolver.Plugin<Annotation> plugin = (AutoArgumentResolver.Plugin<Annotation>) Utils.instantiate(meta.value());
                    return Optional.of(plugin.apply(input, annotation));
                }
            }
            return Optional.empty();
        }
    }
}
