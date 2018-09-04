package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.models.mapper.IntentMapper;
import com.amazon.ask.models.mapper.IntentParseException;
import com.amazon.ask.mvc.annotation.argument.Slot;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.util.ValidationUtils;

import java.util.Optional;

/**
 * Tries to resolve a parameter annotated with @Slot as a registered model slot
 */
public class SlotModelArgumentResolver implements ArgumentResolver {
    protected final IntentMapper intentMapper;

    public SlotModelArgumentResolver(IntentMapper intentMapper) {
        this.intentMapper = ValidationUtils.assertNotNull(intentMapper, "model");
    }

    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        try {
            if (input.requestTypeEquals(IntentRequest.class)) {
                Optional<Slot> annotation = input.getMethodParameter().findAnnotation(Slot.class);
                if (annotation.isPresent()) {
                    return Optional.of(intentMapper.parseIntentSlot((IntentRequest) input.unwrapRequest(), annotation.get().value()));
                }
            }
        } catch (IntentParseException e) {
        }
        return Optional.empty();
    }
}
