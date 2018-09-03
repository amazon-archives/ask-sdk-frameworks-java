package com.amazon.ask.mvc.argument;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.mvc.annotation.argument.SlotValues;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Resolves an argument as a map from slot name to slot value, if the parameter
 * type is {@link java.util.Map} and it is annotated with {@link SlotValues}
 *
 * @author musachyb@
 */
public class SlotValuesArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.requestTypeEquals(IntentRequest.class)
            && input.parameterTypeEquals(Map.class)
            && input.getMethodParameter().findAnnotation(SlotValues.class).isPresent()) {

            IntentRequest request = (IntentRequest) input.unwrapRequest();


            return Optional.of(request.getIntent()
                .getSlots().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue())));
        } else {
            return Optional.empty();
        }
    }
}
