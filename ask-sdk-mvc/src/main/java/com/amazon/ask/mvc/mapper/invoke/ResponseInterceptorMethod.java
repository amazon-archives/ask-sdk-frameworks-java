package com.amazon.ask.mvc.mapper.invoke;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * A {@link ResponseInterceptor} that encapsulates a method's reflective information
 * and a list of {@link ArgumentResolver}s, which are used to resolve arguments and
 * invoke the method as an interceptor.
 *
 * Implements equals and hashCode to enable de-duplication.
 *
 * @see ControllerMethodContext
 * @see MethodInvoker#invoke(HandlerInput, ControllerMethodContext, ArgumentResolver...)
 */
public class ResponseInterceptorMethod implements ResponseInterceptor {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final ControllerMethodContext context;
    protected final MethodInvoker invoker;

    public ResponseInterceptorMethod(ControllerMethodContext context, MethodInvoker invoker) {
        this.context = assertNotNull(context, "context");
        this.invoker = invoker == null ? MethodInvoker.getInstance() : invoker;
    }

    @Override
    public void process(HandlerInput input, Optional<Response> response) {
        ArgumentResolver responseResolver = context ->
            context.parameterTypeIsAssignableFrom(Response.class)
                ? Optional.of(response.orElse(null))
                : Optional.empty();

        try {
            invoker.invoke(input, context, responseResolver);
        } catch (Exception ex) {
            logger.error(String.format("[%s] Failed to invoke response interceptor: %s",
                input.getRequestEnvelope().getRequest().getRequestId(), context), ex);
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
        ResponseInterceptorMethod that = (ResponseInterceptorMethod) o;
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

        public ResponseInterceptorMethod build() {
            return new ResponseInterceptorMethod(context, invoker);
        }
    }
}
