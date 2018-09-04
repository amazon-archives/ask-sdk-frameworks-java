package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.models.mapper.IntentMapper;
import com.amazon.ask.models.mapper.IntentParseException;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.util.ValidationUtils;

import java.util.Optional;

/**
 * Tries to resolve an argument as a registered Intent model
 */
public class IntentModelArgumentResolver implements ArgumentResolver {
    protected final IntentMapper intentMapper;

    public IntentModelArgumentResolver(IntentMapper intentMapper) {
        this.intentMapper = ValidationUtils.assertNotNull(intentMapper, "model");
    }

    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        try {
            if (input.requestTypeEquals(IntentRequest.class)) {
                Object model = intentMapper.parseIntent((IntentRequest) input.unwrapRequest());
                if (input.getMethodParameter().getType().isInstance(model)) {
                    return Optional.of(model);
                }
            }
        } catch (IntentParseException e) {
        }
        return Optional.empty();
    }
}
