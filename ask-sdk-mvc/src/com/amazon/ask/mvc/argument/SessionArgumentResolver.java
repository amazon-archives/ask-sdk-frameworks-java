package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.Session;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

/**
 * Resolves argument of type {@link com.amazon.ask.model.Session}
 *
 * @author musachyb@
 */
public class SessionArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.parameterTypeEquals(Session.class)) {
            return Optional.of(input.getHandlerInput().getRequestEnvelope().getSession());
        }
        return Optional.empty();
    }
}