package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

public class ServiceClientFactoryArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext context) {
        if (context.parameterTypeEquals(ServiceClientFactory.class)) {
            return Optional.of(context.getHandlerInput().getServiceClientFactory());
        }
        return Optional.empty();
    }
}
