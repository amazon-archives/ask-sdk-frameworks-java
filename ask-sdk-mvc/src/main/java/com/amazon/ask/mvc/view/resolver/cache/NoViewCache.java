package com.amazon.ask.mvc.view.resolver.cache;

import com.amazon.ask.mvc.view.View;

import java.util.concurrent.Callable;

/**
 * A singleton view cache that performs no caching.
 */
public class NoViewCache implements ViewCache {
    public static NoViewCache INSTANCE = new NoViewCache();

    public static NoViewCache getInstance() {
        return INSTANCE;
    }

    private NoViewCache() {}

    @Override
    public View getOrCreate(String viewName, Callable<View> callable) throws Exception {
        return callable.call();
    }
}
