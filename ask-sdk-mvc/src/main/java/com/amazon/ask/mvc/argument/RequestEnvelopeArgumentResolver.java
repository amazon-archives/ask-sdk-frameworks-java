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

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;

import java.util.Optional;

/**
 * Resolves arguments of type {@link com.amazon.ask.model.RequestEnvelope}
 *
 * @author musachyb@
 */
public class RequestEnvelopeArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.parameterTypeEquals(RequestEnvelope.class)) {
            return Optional.of(input.getHandlerInput().getRequestEnvelope());
        }
        return Optional.empty();
    }
}
