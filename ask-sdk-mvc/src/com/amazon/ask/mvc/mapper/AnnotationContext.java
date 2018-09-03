package com.amazon.ask.mvc.mapper;

import com.amazon.ask.mvc.SkillContext;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 *
 */
public interface AnnotationContext {
    SkillContext getSkillContext();

    Stream<Annotation> scanAnnotations();
}
