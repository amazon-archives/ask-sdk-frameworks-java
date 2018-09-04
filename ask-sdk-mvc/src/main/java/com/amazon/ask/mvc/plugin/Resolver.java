package com.amazon.ask.mvc.plugin;

import java.util.Optional;

/**
 *
 */
public interface Resolver<C, T> {
    Optional<T> resolve(C context);
}
