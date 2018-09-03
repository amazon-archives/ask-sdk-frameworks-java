package com.amazon.ask.mvc.annotation.mapping;

import com.amazon.ask.mvc.annotation.plugin.AutoResponseInterceptor;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.mapper.invoke.ResponseInterceptorMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@AutoResponseInterceptor(ResponseInterceptor.Plugin.class)
public @interface ResponseInterceptor {

    /**
     * TODO
     */
    class Plugin implements AutoResponseInterceptor.Plugin<ResponseInterceptor> {
        @Override
        public com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor apply(ControllerMethodContext context, ResponseInterceptor responseInterceptor) {
            return ResponseInterceptorMethod.builder().withContext(context).build();
        }
    }
}
