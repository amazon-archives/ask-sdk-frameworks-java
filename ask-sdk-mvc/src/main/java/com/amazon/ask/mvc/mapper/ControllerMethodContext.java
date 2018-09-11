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

import com.amazon.ask.mvc.SkillContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class ControllerMethodContext implements AnnotationContext {
    protected final SkillContext skillContext;
    protected final Object controller;
    protected final Method method;
    protected final List<MethodParameter> parameters;

    private ControllerMethodContext(SkillContext skillContext, Object controller, Method method) {
        this.skillContext = assertNotNull(skillContext, "skillContext");
        this.controller = assertNotNull(controller, "controller");
        this.method = assertNotNull(method, "method");

        this.parameters = Collections.unmodifiableList(resolveMethodParameters());
    }

    protected List<MethodParameter> resolveMethodParameters() {
        Annotation[][] parameterAnnotations = this.method.getParameterAnnotations();
        //build a list of MethodParameters so we can cache them
        Class[] parameterTypes = this.method.getParameterTypes();
        List<MethodParameter> parameters = new ArrayList<>(parameterTypes.length);

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters.add(new MethodParameter(this.method, i, parameterTypes[i], parameterAnnotations[i]));
        }
        return parameters;
    }

    @Override
    public SkillContext getSkillContext() {
        return skillContext;
    }

    @Override
    public Stream<Annotation> scanAnnotations() {
        return Arrays.stream(method.getAnnotations());
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    /**
     * @return Information about the parameters to the method, so reflection doesn't need to be used
     *         on each invocation to resolve them.
     */
    public List<MethodParameter> getParameters() {
        return parameters;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private SkillContext skillContext;
        private Object controller;
        private Method method;

        private Builder() {
        }

        public Builder withSkillContext(SkillContext skillContext) {
            this.skillContext = skillContext;
            return this;
        }

        public Builder withController(Object controller) {
            this.controller = controller;
            return this;
        }

        public Builder withMethod(Method method) {
            this.method = method;
            return this;
        }

        public ControllerMethodContext build() {
            return new ControllerMethodContext(skillContext, controller, method);
        }
    }
}