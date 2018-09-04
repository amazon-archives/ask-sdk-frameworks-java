package com.amazon.ask.mvc.annotation.plugin;

import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.ResponseInterceptorResolver;

import java.lang.annotation.*;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Added to an annotation that identifies a controller's method as a {@link ResponseInterceptor}
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoResponseInterceptor {
    Class<? extends Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<ControllerMethodContext, A, ResponseInterceptor> {}

    class Scanner implements ResponseInterceptorResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Optional<ResponseInterceptor> resolve(ControllerMethodContext context) {
            for (Annotation annotation : context.getMethod().getAnnotations()) {
                AutoResponseInterceptor meta = annotation.annotationType().getAnnotation(AutoResponseInterceptor.class);
                if (meta != null) {
                    AutoResponseInterceptor.Plugin<Annotation> plugin = (AutoResponseInterceptor.Plugin<Annotation>) Utils.instantiate(meta.value());
                    return Optional.of(plugin.apply(context, annotation));
                }
            }
            return Optional.empty();
        }
    }
}
