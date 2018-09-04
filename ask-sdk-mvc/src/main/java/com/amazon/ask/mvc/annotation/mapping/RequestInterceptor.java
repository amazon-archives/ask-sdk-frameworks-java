package com.amazon.ask.mvc.annotation.mapping;

import com.amazon.ask.mvc.annotation.plugin.AutoRequestInterceptor;
import com.amazon.ask.mvc.mapper.invoke.RequestInterceptorMethod;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register a controller's method as a {@link com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@AutoRequestInterceptor(RequestInterceptor.Plugin.class)
public @interface RequestInterceptor {

    class Plugin implements AutoRequestInterceptor.Plugin<RequestInterceptor> {
        @Override
        public com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor apply(ControllerMethodContext context, RequestInterceptor annotation) {
            return RequestInterceptorMethod.builder().withContext(context).build();
        }
    }
}
