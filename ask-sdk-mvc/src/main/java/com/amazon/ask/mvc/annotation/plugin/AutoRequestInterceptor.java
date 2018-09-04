package com.amazon.ask.mvc.annotation.plugin;

import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.RequestInterceptorResolver;

import java.lang.annotation.*;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Added to an annotation that identifies a controller's method as a {@link RequestInterceptor}
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRequestInterceptor {
    Class<? extends Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<ControllerMethodContext, A, RequestInterceptor> {}

    class Scanner implements RequestInterceptorResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Optional<RequestInterceptor> resolve(ControllerMethodContext context) {
            for (Annotation annotation : context.getMethod().getAnnotations()) {
                AutoRequestInterceptor meta = annotation.annotationType().getAnnotation(AutoRequestInterceptor.class);
                if (meta != null) {
                    AutoRequestInterceptor.Plugin<Annotation> plugin = (AutoRequestInterceptor.Plugin<Annotation>) Utils.instantiate(meta.value());
                    return Optional.of(plugin.apply(context, annotation));
                }
            }
            return Optional.empty();
        }
    }
}
