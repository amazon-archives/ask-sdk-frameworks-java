package com.amazon.ask.mvc.argument;

import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Optional;

public class ResponseBuilderArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.parameterTypeEquals(ResponseBuilder.class)) {
            return Optional.of(input.getHandlerInput().getResponseBuilder());
        }
        return Optional.empty();
    }
}
