package com.amazon.ask.mvc.plugin;

import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.util.Optional;
import java.util.function.Function;

/**
 *
 */
public interface ResponseInterceptorResolver extends Resolver<ControllerMethodContext, ResponseInterceptor> {
}
