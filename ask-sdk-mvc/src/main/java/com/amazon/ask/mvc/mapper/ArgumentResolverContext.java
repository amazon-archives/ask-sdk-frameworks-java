/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.SkillContext;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Contains information about the argument to be resolved
 */
public class ArgumentResolverContext {
    protected final SkillContext skillContext;
    protected final MethodParameter methodParameter;
    protected final HandlerInput handlerInput;

    public ArgumentResolverContext(SkillContext skillContext, MethodParameter methodParameter, HandlerInput handlerInput) {
        this.skillContext = assertNotNull(skillContext, "skillContext");
        this.methodParameter = assertNotNull(methodParameter, "methodParameter");
        this.handlerInput = assertNotNull(handlerInput, "handlerInput");
    }

    /**
     * @return the skill context
     */
    public SkillContext getSkillContext() {
        return skillContext;
    }

    /**
     * @return the method parameter which contains information about the parameter itself
     */
    public MethodParameter getMethodParameter() {
        return methodParameter;
    }

    /**
     * @return the current input to the dispatcher for the current request
     */
    public HandlerInput getHandlerInput() {
        return handlerInput;
    }

    /**
     * @return the original request inside the {@link HandlerInput} instance
     */
    public Request unwrapRequest() {
        return handlerInput.getRequestEnvelope().getRequest();
    }

    /**
     * @param type request type
     * @return true if the request contained inside the {@link RequestEnvelope} is an instance of the specified type
     */
    public boolean requestTypeEquals(Class<? extends Request> type) {
        return type.isInstance(unwrapRequest());
    }

    /**
     * @param type parameter type
     * @return true if the type of the parameter inside {@link MethodParameter} matches the specified type
     */
    public boolean parameterTypeEquals(Class type) {
        return type.equals(methodParameter.getType());
    }

    /**
     * @param type parameter type
     * @return true if the type of the parameter inside {@link MethodParameter} is assignable from the specified type
     */
    public boolean parameterTypeIsAssignableFrom(Class type) {
        return methodParameter.getType().isAssignableFrom(type);
    }
}
