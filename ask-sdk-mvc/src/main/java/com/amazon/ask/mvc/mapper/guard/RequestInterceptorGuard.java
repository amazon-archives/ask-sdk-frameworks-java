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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;

import java.util.function.Predicate;

/**
 *
 */
public class RequestInterceptorGuard extends Guard<RequestInterceptor> implements RequestInterceptor {
    public RequestInterceptorGuard(RequestInterceptor delegate, Predicate<HandlerInput> predicate, int priority) {
        super(delegate, predicate, priority);
    }

    @Override
    public void process(HandlerInput input) {
        if (predicate.test(input)) {
            delegate.process(input);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Guard.Builder<Builder, RequestInterceptor, RequestInterceptorGuard> {
        private Builder() {
        }

        public RequestInterceptorGuard build() {
            return new RequestInterceptorGuard(delegate, predicate, priority);
        }
    }
}
