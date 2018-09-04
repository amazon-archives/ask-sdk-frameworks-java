package com.amazon.ask.mvc.annotation.argument;

import com.amazon.ask.mvc.annotation.plugin.AutoArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * If added to a controller parameter of type {@link java.util.Map}, a Map<String, Object>
 * containing the session attributes and their values will be passed in.
 */
@Target(value = {PARAMETER})
@Retention(value = RUNTIME)
@Documented
@AutoArgumentResolver(SessionAttributes.Plugin.class)
public @interface SessionAttributes {
    class Plugin implements AutoArgumentResolver.Plugin<SessionAttributes> {
        @Override
        public Object apply(ArgumentResolverContext input, SessionAttributes sessionAttributes) {
            return input
                .getHandlerInput()
                .getRequestEnvelope()
                .getSession()
                .getAttributes();
        }
    }
}
