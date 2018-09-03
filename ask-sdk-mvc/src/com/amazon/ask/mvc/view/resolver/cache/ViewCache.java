package com.amazon.ask.mvc.view.resolver.cache;

import com.amazon.ask.mvc.view.View;

import java.util.concurrent.Callable;

/**
 * Interface for caching resolved views.
 *
 * @see NoViewCache
 * @see SingleThreadedViewCache
 * @see ConcurrentViewCache
 */
public interface ViewCache {
    /** default cache capacity of 1024 resolved views **/
    int DEFAULT_CACHE_CAPACITY = 1024;

    /**
     * Get the cached view or create it
     *
     * @param key cache key of this view
     * @param callable to create view if it does not exist in the cache
     * @return the view
     * @throws Exception if there was an exception creating the view
     */
    View getOrCreate(String key, Callable<View> callable) throws Exception;
}
