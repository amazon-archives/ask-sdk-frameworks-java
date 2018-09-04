package com.amazon.ask.mvc.view.resolver.cache;

import com.amazon.ask.mvc.view.View;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.amazon.ask.util.ValidationUtils.assertIsPositive;

/**
 * A non-thread-safe cache for skills running in a single-threaded environment such as AWS Lambda
 */
public class SingleThreadedViewCache implements ViewCache {
    private final Map<String, View> cache;

    public SingleThreadedViewCache(Builder builder) {
        this(builder.capacity);
    }

    public SingleThreadedViewCache(int capacity) {
        assertIsPositive(capacity, "capacity");
        this.cache = new LinkedHashMap<String, View>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, View> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public View getOrCreate(String key, Callable<View> callable) throws Exception {
        View view = cache.get(key);
        if (view == null) {
            view = callable.call();
            cache.put(key, view);
        }
        return view;
    }

    /**
     * @return a single threaded view cache with default capacity
     */
    public static SingleThreadedViewCache defaultCache() {
        return SingleThreadedViewCache.builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int capacity = DEFAULT_CACHE_CAPACITY;

        public Builder withCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public SingleThreadedViewCache build() {
            return new SingleThreadedViewCache(this);
        }
    }
}
