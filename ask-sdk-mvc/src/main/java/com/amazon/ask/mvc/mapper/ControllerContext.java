package com.amazon.ask.mvc.mapper;

import com.amazon.ask.models.Utils;
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