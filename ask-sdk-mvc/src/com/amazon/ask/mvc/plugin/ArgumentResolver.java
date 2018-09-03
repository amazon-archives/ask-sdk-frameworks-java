package com.amazon.ask.mvc.plugin;

import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

/**
 * Resolves the value to be passed in to a controller method, based on type, annotations etc
 */
public interface ArgumentResolver extends Resolver<ArgumentResolverContext, Object> {
}
