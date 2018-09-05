/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper.invoke;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.view.ViewRenderer;
import com.amazon.ask.mvc.plugin.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Base class for {@link ExceptionHandler}s that invoke a method with reflection.
 *
 * The exception is made available as an {@link ArgumentResolver}
 *
 * Responses from may also be views, fulfilled by a {@link ViewResolver}.
 */
public abstract class ExceptionHandlerMethod implements ExceptionHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final ControllerMethodContext context;
    protected final MethodInvoker invoker;
    protected final ViewRenderer renderer;

    public ExceptionHandlerMethod(ControllerMethodContext context) {
        this(context, null, null);
    }
    public ExceptionHandlerMethod(ControllerMethodContext context, MethodInvoker invoker, ViewRenderer renderer) {
        this.context = assertNotNull(context, "context");
        this.invoker = invoker == null ? MethodInvoker.getInstance() : invoker;
        this.renderer = renderer == null ? ViewRenderer.getInstance() : renderer;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {
        ArgumentResolver exceptionResolver = context ->
            context.parameterTypeIsAssignableFrom(throwable.getClass())
                ? Optional.of(throwable)
                : Optional.empty();

        try {
            return renderer.render(context, invoker.invoke(input, context, exceptionResolver), input.getRequestEnvelope());
        } catch (Exception ex) {
            logger.error(String.format("[%s] Failed to handle exception in ExceptionHandler: %s",
                input.getRequestEnvelope().getRequest().getRequestId() ,context.getMethod()), ex);
            throw new RuntimeException(ex);
        }
    }
}
