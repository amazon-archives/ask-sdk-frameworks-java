package com.amazon.ask.mvc.mapper.guard;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.model.Response;

import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 */
public class ResponseInterceptorGuard extends Guard<ResponseInterceptor> implements ResponseInterceptor {
    public ResponseInterceptorGuard(ResponseInterceptor delegate, Predicate<HandlerInput> predicate, int priority) {
        super(delegate, predicate, priority);
    }

    @Override
    public void process(HandlerInput input, Optional<Response> response) {
        if (predicate.test(input)) {
            delegate.process(input, response);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Guard.Builder<Builder, ResponseInterceptor, ResponseInterceptorGuard> {
        private Builder() {
        }

        public ResponseInterceptorGuard build() {
            return new ResponseInterceptorGuard(delegate, predicate, priority);
        }
    }
}
