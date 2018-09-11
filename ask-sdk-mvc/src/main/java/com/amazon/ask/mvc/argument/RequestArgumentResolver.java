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

import com.amazon.ask.model.Request;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.util.Optional;

/**
 * Resolves arguments that are sub types of {@link Request}, only if the
 * param's is of the same type as the envelope request's type.
 *
 * @author musachyb@
 */
public class RequestArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (Request.class.isAssignableFrom(input.getMethodParameter().getType()) && (input.parameterTypeEquals(input.unwrapRequest().getClass()))) {
            return Optional.of(input.getHandlerInput().getRequestEnvelope().getRequest());
        }
        return Optional.empty();
    }
}
