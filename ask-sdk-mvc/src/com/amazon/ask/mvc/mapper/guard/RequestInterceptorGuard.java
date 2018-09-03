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
