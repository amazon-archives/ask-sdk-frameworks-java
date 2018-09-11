/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.annotation.mapping;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.mvc.annotation.plugin.AutoRequestHandler;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.mapper.invoke.MethodInvoker;
import com.amazon.ask.mvc.mapper.invoke.RequestHandlerMethod;
import com.amazon.ask.mvc.view.ViewRenderer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for mapping Intent Requests to methods.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
@AutoRequestHandler(IntentMapping.Plugin.class)
public @interface IntentMapping {
    /**
     * @return name of intent
     */
    String name() default "";

    /**
     * @return type of intent
     * @see Intent
     */
    Class<?> type() default Object.class;

    /**
     * Resolve a {@link RequestHandler} from a method annotated with {@link IntentMapping}.
     *
     * @see IntentMapping.Handler for the {@link RequestHandler} implementation.
     */
    class Plugin implements AutoRequestHandler.Plugin<IntentMapping> {
        @Override
        public RequestHandler apply(ControllerMethodContext context, IntentMapping annotation) {
            String intentName = annotation.name();
            Class<?> intentType = annotation.type();

            if (intentName.isEmpty() && Object.class.equals(intentType)) {
                throw new IllegalArgumentException(String.format("The @IntentMapping annotation in method '%s:%s' must define a non empty string in " +
                    "the 'value' property or the intent type in the 'intentType' property", context.getController().getClass().getName(), context.getMethod().getName()));
            }

            //if the mapping is by type, we build a value for the type and use that for the mapping
            if (intentName.isEmpty()) {
                intentName = buildIntentNameByType(intentType);
            }

            return new Handler(context, intentName);
        }

        protected String buildIntentNameByType(Class<?> intentType) {
            Class<?> currentType = intentType;
            while(currentType != Object.class) {
                Intent annotation = currentType.getAnnotation(Intent.class);

                if (annotation != null) {
                    if (annotation.value().isEmpty()) {
                        return currentType.getSimpleName();
                    } else {
                        return annotation.value();
                    }
                }

                if (currentType == Object.class) {
                    break;
                }

                currentType = currentType.getSuperclass();
            }

            throw new IllegalArgumentException(String.format("The intent '%s' is missing an Intent annotation", intentType.getName()));
        }

    }

    /**
     * Invoke a controller's method to handle a request.
     *
     * This class primarily implements {@link #canHandle(HandlerInput)} to filter intent requests by name.
     *
     * @see RequestHandlerMethod for the invocation logic.
     */
    class Handler extends RequestHandlerMethod {
        private final String intentName;

        public Handler(ControllerMethodContext context, String intentName) {
            this(context, intentName, MethodInvoker.getInstance(), ViewRenderer.getInstance());
        }

        public Handler(ControllerMethodContext context, String intentName, MethodInvoker invoker, ViewRenderer viewRenderer) {
            super(context, invoker, viewRenderer);
            this.intentName = assertNotNull(intentName, "intentName");
        }

        @Override
        public boolean canHandle(HandlerInput input) {
            Request request = input.getRequestEnvelope().getRequest();
            if (request instanceof IntentRequest) {
                IntentRequest intentRequest = (IntentRequest) request;
                return intentRequest.getIntent().getName().equals(intentName);
            } else {
                return false;
            }
        }
    }
}
