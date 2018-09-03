package com.amazon.ask.mvc.plugin;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.mvc.mapper.AnnotationContext;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 */
public interface PredicateResolver extends Resolver<AnnotationContext, Predicate<HandlerInput>> {
}
