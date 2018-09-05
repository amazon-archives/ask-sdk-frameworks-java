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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.view.ViewRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Base class for a {@link RequestHandler} that invokes a controller's method to handle a request.
 */
public abstract class RequestHandlerMethod implements RequestHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ControllerMethodContext context;
    protected final MethodInvoker invoker;
    protected final ViewRenderer renderer;

    public RequestHandlerMethod(ControllerMethodContext context, MethodInvoker invoker, ViewRenderer renderer) {
        this.context = assertNotNull(context, "context");
        this.invoker = invoker == null ? MethodInvoker.getInstance() : invoker;
        this.renderer = renderer == null ? ViewRenderer.getInstance() : renderer;
    }

    /**
     * Handles a request by invoking a method using reflection.
     *
     * @param input request envelope containing request, context and state
     * @return response if exists, otherwise empty
     * @see MethodInvoker for invocation and argument resolution logic.
     */
    @Override
    public Optional<Response> handle(HandlerInput input) {
        logger.trace("[{}] Invoking '{}:{}'", input.getRequestEnvelope().getRequest().getRequestId(), context.getMethod().getDeclaringClass().getName(), context.getMethod().getName());
        Object output = invoker.invoke(input, context);
        return renderer.render(context, output, input.getRequestEnvelope());
    }
}
