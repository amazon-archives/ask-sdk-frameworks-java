/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper.guard;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.mapper.HasPriority;

import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 */
public class ExceptionHandlerGuard extends Guard<ExceptionHandler> implements ExceptionHandler, HasPriority {
    public ExceptionHandlerGuard(ExceptionHandler delegate, Predicate<HandlerInput> predicate, int priority) {
        super(delegate, predicate, priority);
    }

    @Override
    public boolean canHandle(HandlerInput input, Throwable throwable) {
        return predicate.test(input) && delegate.canHandle(input, throwable);
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {
        return delegate.handle(input, throwable);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Guard.Builder<Builder, ExceptionHandler, ExceptionHandlerGuard> {
        @Override
        public ExceptionHandlerGuard build() {
            return new ExceptionHandlerGuard(delegate, predicate, priority);
        }
    }
}
