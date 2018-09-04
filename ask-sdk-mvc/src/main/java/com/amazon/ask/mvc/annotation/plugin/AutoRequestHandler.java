package com.amazon.ask.mvc.annotation.plugin;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.plugin.RequestHandlerResolver;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.lang.annotation.*;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Added to an annotation that identifies a controller's method as a {@link RequestHandler}.
 *
 * @see IntentMapping
 * @see RequestMapping
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AutoRequestHandler {
    Class<? extends Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation> extends BiFunction<ControllerMethodContext, A, RequestHandler> {}

    class Scanner implements RequestHandlerResolver {
        @Override
        public Optional<RequestHandler> resolve(ControllerMethodContext context) {
            for (Annotation annotation : context.getMethod().getAnnotations()) {
                AutoRequestHandler meta = annotation.annotationType().getAnnotation(AutoRequestHandler.class);
                if (meta != null) {
                    AutoRequestHandler.Plugin<Annotation> plugin = (AutoRequestHandler.Plugin<Annotation>) Utils.instantiate(meta.value());
                    return Optional.of(plugin.apply(context, annotation));
                }
            }
            return Optional.empty();
        }
    }
}
