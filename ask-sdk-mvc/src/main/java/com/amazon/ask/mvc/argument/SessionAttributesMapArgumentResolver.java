package com.amazon.ask.mvc.argument;

import com.amazon.ask.mvc.annotation.argument.SessionAttributes;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Map;
import java.util.Optional;

/**
 * Resolves an argument as a map with the session attributes, if the parameter
 * type is {@link Map} and it is annotated with {@link SessionAttributes}
 */
public class SessionAttributesMapArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.parameterTypeEquals(Map.class)
            && input.getMethodParameter().findAnnotation(SessionAttributes.class).isPresent()) {

            return Optional.of(input.getHandlerInput().getAttributesManager().getSessionAttributes());
        }
        return Optional.empty();
    }
}
