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
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.mapper.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Resolve a method's arguments using {@link ArgumentResolver}s and invoke it, returning the result.
 */
public class MethodInvoker {
    private static final MethodInvoker INSTANCE = new MethodInvoker();
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected MethodInvoker() {
    }

    /**
     * @return singleton instance
     */
    public static MethodInvoker getInstance() {
        return INSTANCE;
    }

    /**
     * Resolve a method's arguments using {@link ArgumentResolver}s and invoke it, returning the result.
     *
     * @param input handler input
     * @param context controller method context
     * @param extraResolvers extra argument resolvers to consider
     * @return result object
     * @see MethodInvoker#invoke(HandlerInput, ControllerMethodContext, List)
     */
    public Object invoke(HandlerInput input, ControllerMethodContext context, ArgumentResolver... extraResolvers) {
        return invoke(input, context, Arrays.asList(extraResolvers));
    }

    /**
     * Resolve a method's arguments using {@link ArgumentResolver}s and invoke it, returning the result.
     *
     * @param input handler input
     * @param context controller method context
     * @param extraResolvers extra argument resolvers to consider
     * @return result object
     */
    public Object invoke(HandlerInput input, ControllerMethodContext context, List<ArgumentResolver> extraResolvers) {
        Method method = context.getMethod();
        Class[] parameterTypes = method.getParameterTypes();
        List<MethodParameter> methodParameters = context.getParameters();
        Object[] params = new Object[parameterTypes.length];

        //resolve the param types
        for (int i = 0; i < parameterTypes.length; i++) {
            Class paramType = parameterTypes[i];
            MethodParameter methodParameter = methodParameters.get(i);

            //find a resolver for this param, and get its value
            ArgumentResolverContext resolverInput = new ArgumentResolverContext(context.getSkillContext(), methodParameter, input);
            Optional<Object> paramValue = Stream.concat(context.getSkillContext().getArgumentResolvers().stream(), extraResolvers.stream())
                .flatMap(resolver -> resolver.resolve(resolverInput).map(Stream::of).orElse(Stream.empty()))
                .findFirst();

            if (!paramValue.isPresent()) {
                throw new UnresolvedParameterException(i, paramType, method);
            }

            params[i] = paramValue.get();
        }

        String requestId = input.getRequestEnvelope().getRequest().getRequestId();
        //handle it
        logger.trace("[{}] Invoking '{}:{}'", requestId, method.getDeclaringClass().getName(), method.getName());
        try {
            return method.invoke(context.getController(), params);
        } catch (InvocationTargetException ex) {
            Throwable targetException = ex.getTargetException();
            logger.error(String.format("[%s] Reflective call failed when invoking: %s#%s", requestId, context.getController().getClass().getName(), context.getMethod()), targetException);
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            } else if (targetException instanceof Error) {
                throw (Error) targetException;
            } else {
                throw new RuntimeException(targetException);
            }
        } catch (IllegalAccessException e) {
            logger.error(String.format("[%s] Reflective call failed when invoking: %s#%s", requestId, context.getController().getClass().getName(), context.getMethod()), e);
            throw new IllegalStateException(e);
        }
    }
}
