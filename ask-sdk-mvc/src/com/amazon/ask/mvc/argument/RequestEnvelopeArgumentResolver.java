package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

/**
 * Resolves arguments of type {@link com.amazon.ask.model.RequestEnvelope}
 *
 * @author musachyb@
 */
public class RequestEnvelopeArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.parameterTypeEquals(RequestEnvelope.class)) {
            return Optional.of(input.getHandlerInput().getRequestEnvelope());
        }
        return Optional.empty();
    }
}
