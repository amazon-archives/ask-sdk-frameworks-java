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
