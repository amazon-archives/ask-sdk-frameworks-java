package com.amazon.ask.mvc.argument;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

/**
 * Resolves arguments of type {@link com.amazon.ask.attributes.AttributesManager}
 */
public class AttributesManagerArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext context) {
        if (context.parameterTypeEquals(AttributesManager.class)) {
            return Optional.of(context.getHandlerInput().getAttributesManager());
        }
        return Optional.empty();
    }
}
