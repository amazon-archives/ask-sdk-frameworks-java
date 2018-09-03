package com.amazon.ask.mvc.mapper.guard;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.mapper.HasPriority;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 */
public class RequestHandlerGuard extends Guard<RequestHandler> implements RequestHandler, HasPriority {
    private RequestHandlerGuard(RequestHandler delegate, Predicate<HandlerInput> predicate, int priority) {
        super(delegate, predicate, priority);
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return predicate.test(input) && delegate.canHandle(input);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return delegate.handle(input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestHandlerGuard that = (RequestHandlerGuard) o;
        return priority == that.priority &&
            Objects.equals(delegate, that.delegate) &&
            Objects.equals(predicate, that.predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate, predicate, priority);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Guard.Builder<Builder, RequestHandler, RequestHandlerGuard> {
        private Builder() {
        }
        public RequestHandlerGuard build() {
            return new RequestHandlerGuard(delegate, predicate, priority);
        }
    }
}
