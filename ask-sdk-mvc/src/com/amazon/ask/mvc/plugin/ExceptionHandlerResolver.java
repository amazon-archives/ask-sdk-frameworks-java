package com.amazon.ask.mvc.plugin;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.util.Optional;
import java.util.function.Function;

/**
 *
 */
public interface ExceptionHandlerResolver extends Resolver<ControllerMethodContext, ExceptionHandler> {
}
