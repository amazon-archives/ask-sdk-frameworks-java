package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.Request;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.util.Optional;

/**
 * Resolves arguments that are sub types of {@link Request}, only if the
 * param's is of the same type as the envelope request's type.
 *
 * @author musachyb@
 */
public class RequestArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (Request.class.isAssignableFrom(input.getMethodParameter().getType()) && (input.parameterTypeEquals(input.unwrapRequest().getClass()))) {
            return Optional.of(input.getHandlerInput().getRequestEnvelope().getRequest());
        }
        return Optional.empty();
    }
}
