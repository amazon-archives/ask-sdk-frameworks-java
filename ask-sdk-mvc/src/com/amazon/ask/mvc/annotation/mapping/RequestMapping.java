package com.amazon.ask.mvc.annotation.mapping;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.mvc.annotation.plugin.AutoRequestHandler;
import com.amazon.ask.mvc.mapper.invoke.MethodInvoker;
import com.amazon.ask.mvc.mapper.invoke.RequestHandlerMethod;
import com.amazon.ask.mvc.view.ViewRenderer;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for mapping requests by type to methods.
 *
 * To map {@link IntentRequest} use {@link IntentMapping} instead.
 *
 * @see AutoRequestHandler to understand how this mapping is discovered
 * @see Plugin for the resolution implementation
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
@AutoRequestHandler(RequestMapping.Plugin.class)
public @interface RequestMapping {
    /**
     * Request types to match
     */
    Class<? extends Request>[] value() default {};

    /**
     * Request types to match. Same as value()
     */
    Class<? extends Request>[] types() default {};

    /**
     * Resolve a {@link RequestHandler} from a method annotated with {@link RequestMapping}.
     *
     * @see Handler for the {@link RequestHandler} implementation.
     */
    class Plugin implements AutoRequestHandler.Plugin<RequestMapping> {
        @Override
        public RequestHandler apply(ControllerMethodContext context, RequestMapping annotation) {
            return new Handler(context, Arrays
                .stream(annotation.types().length == 0 ? annotation.value() : annotation.types())
                .collect(Collectors.toSet()));
        }
    }

    /**
     * Invoke a controller's method to handle a request.
     *
     * This class primarily implements {@link #canHandle(HandlerInput)} to filter requests by their type.
     *
     * @see RequestHandlerMethod for the invocation logic.
     */
    class Handler extends RequestHandlerMethod {
        private final Set<Class<? extends Request>> requestTypes;

        public Handler(ControllerMethodContext context, Set<Class<? extends Request>> requestTypes) {
            this(context, requestTypes, MethodInvoker.getInstance(), ViewRenderer.getInstance());

        }
        public Handler(ControllerMethodContext context, Set<Class<? extends Request>> requestTypes, MethodInvoker invoker, ViewRenderer renderer) {
            super(context, invoker, renderer);
            this.requestTypes = assertNotNull(requestTypes, "requestTypes");
        }

        @Override
        public boolean canHandle(HandlerInput input) {
            return requestTypes.contains(input.getRequestEnvelope().getRequest().getClass());
        }
    }
}
