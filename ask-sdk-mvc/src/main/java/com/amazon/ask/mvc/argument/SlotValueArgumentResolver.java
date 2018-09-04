package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.mvc.annotation.argument.Slot;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.util.Optional;

/**
 * Resolves an argument as the value of a slot, if the type of the parameter
 * is String and it is annotated with {@link Slot}
 *
 * @author musachyb@
 */
public class SlotValueArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.requestTypeEquals(IntentRequest.class)
            && input.parameterTypeEquals(String.class)
            && input.getMethodParameter().findAnnotation(Slot.class).isPresent()) {

            String slotName = input.getMethodParameter().findAnnotation(Slot.class).get().value();
            IntentRequest request = (IntentRequest) input.unwrapRequest();
            return Optional.of(request.getIntent().getSlots().get(slotName).getValue());
        }
        return Optional.empty();
    }
}
