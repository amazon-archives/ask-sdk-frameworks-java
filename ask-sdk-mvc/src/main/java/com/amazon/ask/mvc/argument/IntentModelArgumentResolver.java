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
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.interaction.mapper.IntentParseException;
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
