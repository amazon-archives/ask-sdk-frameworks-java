package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

/**
 * Resolves arguments of type {@link com.amazon.ask.model.Intent}
 *
 * @author musachyb@
 */
public class IntentArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.requestTypeEquals(IntentRequest.class) && input.parameterTypeEquals(Intent.class)) {
            return Optional.of(((IntentRequest) input.unwrapRequest()).getIntent())          ;
        }
        return Optional.empty();
    }
}
