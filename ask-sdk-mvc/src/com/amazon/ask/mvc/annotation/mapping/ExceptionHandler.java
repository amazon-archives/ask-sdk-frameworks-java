package com.amazon.ask.mvc.annotation.mapping;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.mvc.annotation.plugin.AutoExceptionHandler;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.mapper.invoke.ExceptionHandlerMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@AutoExceptionHandler(ExceptionHandler.Plugin.class)
public @interface ExceptionHandler {

    /**
     * @return class of exception to match. Defaults to all.
     */
    Class<? extends Throwable> exception() default Throwable.class;

    /**
     * Switch whether to compare classes by {@link Class#isAssignableFrom(Class)} or {@link Class#equals(Object)};
     *
     * @return whether to match sub classes of {@link #exception()} or not.
     */
    boolean matchSubclasses() default true;

    class Plugin implements AutoExceptionHandler.Plugin<ExceptionHandler> {
        @Override
        public com.amazon.ask.dispatcher.exception.ExceptionHandler apply(ControllerMethodContext context, ExceptionHandler annotation) {
            return new ExceptionHandlerMethod(context) {
                @Override
                public boolean canHandle(HandlerInput input, Throwable throwable) {
                    return annotation.matchSubclasses()
                        ? annotation.exception().isInstance(throwable)
                        : annotation.exception().equals(throwable.getClass());
                }
            };
        }
    }
}
