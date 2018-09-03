package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

/**
 * Resolves arguments of type {@link Slot} with a {@link com.amazon.ask.mvc.annotation.argument.Slot} annotation
 *
 * @author musachyb@
 */
public class SlotArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.requestTypeEquals(IntentRequest.class)
            && input.parameterTypeEquals(Slot.class)
            && input.getMethodParameter().findAnnotation(com.amazon.ask.mvc.annotation.argument.Slot.class).isPresent()) {

            String slotName = input.getMethodParameter().findAnnotation(com.amazon.ask.mvc.annotation.argument.Slot.class).get().value();
            IntentRequest request = (IntentRequest) input.unwrapRequest();
            return Optional.of(request.getIntent().getSlots().get(slotName));
        }
        return Optional.empty();
    }
}
