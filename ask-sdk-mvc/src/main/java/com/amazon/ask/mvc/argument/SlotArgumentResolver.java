/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
