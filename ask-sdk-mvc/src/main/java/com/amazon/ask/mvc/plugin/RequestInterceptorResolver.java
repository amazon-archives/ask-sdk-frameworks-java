package com.amazon.ask.mvc.plugin;

import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.util.Optional;
import java.util.function.Function;

/**
 *
 */
public interface RequestInterceptorResolver extends Resolver<ControllerMethodContext, RequestInterceptor> {
}
