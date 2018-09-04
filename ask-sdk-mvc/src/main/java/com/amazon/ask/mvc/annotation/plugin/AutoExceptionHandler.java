package com.amazon.ask.mvc.annotation.plugin;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.models.Utils;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.ExceptionHandlerResolver;

import java.lang.annotation.*;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Added to an annotation that identifies a controller's method as a {@link ExceptionHandler}
 *
 * @see com.amazon.ask.mvc.annotation.mapping.ExceptionHandler
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AutoExceptionHandler {
    Class<? extends Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<ControllerMethodContext, A, ExceptionHandler> {}

    class Scanner implements ExceptionHandlerResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Optional<ExceptionHandler> resolve(ControllerMethodContext context) {
            for (Annotation annotation : context.getMethod().getAnnotations()) {
                AutoExceptionHandler meta = annotation.annotationType().getAnnotation(AutoExceptionHandler.class);
                if (meta != null) {
                    AutoExceptionHandler.Plugin<Annotation> plugin = (AutoExceptionHandler.Plugin<Annotation>) Utils.instantiate(meta.value());
                    return Optional.of(plugin.apply(context, annotation));
                }
            }
            return Optional.empty();
        }
    }
}
