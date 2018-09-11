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

import com.amazon.ask.interaction.Utils;
import com.amazon.ask.mvc.SkillContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class ControllerContext implements AnnotationContext {
    private SkillContext skillContext;
    private final Object controller;

    private ControllerContext(SkillContext skillContext, Object controller) {
        this.skillContext = assertNotNull(skillContext, "skillContext");
        this.controller = assertNotNull(controller, "controller");
    }

    @Override
    public SkillContext getSkillContext() {
        return skillContext;
    }

    @Override
    public Stream<Annotation> scanAnnotations() {
        return Utils.getSuperclasses(controller.getClass()).map(Class::getAnnotations).flatMap(Arrays::stream);
    }

    public Object getController() {
        return controller;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private SkillContext skillContext;
        private Object controller;

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

        public ControllerContext build() {
            return new ControllerContext(skillContext, controller);
        }
    }
}