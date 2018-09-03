package com.amazon.ask.mvc.mapper.invoke;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * A {@link RequestInterceptor} that encapsulates a method's context and invokes it to handle a request.
 *
 * Implements equals and hashCode to enable de-duplication.
 *
 * @see ControllerMethodContext
 * @see MethodInvoker#invoke(HandlerInput, ControllerMethodContext, ArgumentResolver...)
 */
public class RequestInterceptorMethod implements RequestInterceptor {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ControllerMethodContext context;
    protected final MethodInvoker invoker;

    public RequestInterceptorMethod(ControllerMethodContext context, MethodInvoker invoker) {
        this.context = assertNotNull(context, "context");
        this.invoker = invoker == null ? MethodInvoker.getInstance() : invoker;
    }

    @Override
    public void process(HandlerInput input) {
        try {
            invoker.invoke(input, context);
        } catch (Exception ex) {
            logger.error(String.format("[%s] Failed to invoke request interceptor: %s", input.getRequestEnvelope().getRequest().getRequestId(), context), ex);
            throw new RuntimeException(ex);
        }
    }

    public ControllerMethodContext getContext() {
        return context;
    }

    public MethodInvoker getInvoker() {
        return invoker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestInterceptorMethod that = (RequestInterceptorMethod) o;
        return Objects.equals(context, that.context) &&
            Objects.equals(invoker, that.invoker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, invoker);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ControllerMethodContext context;
        private MethodInvoker invoker;

        private Builder() {
        }

        public Builder withContext(ControllerMethodContext context) {
            this.context = context;
            return this;
        }

        public Builder withInvoker(MethodInvoker invoker) {
            this.invoker = invoker;
            return this;
        }

        public RequestInterceptorMethod build() {
            return new RequestInterceptorMethod(context, invoker);
        }
    }
}
