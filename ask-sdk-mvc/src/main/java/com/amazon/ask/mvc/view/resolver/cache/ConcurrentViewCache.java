/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view.resolver.cache;

import com.amazon.ask.mvc.view.View;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe view cache for concurrent skill environments such as servlets.
 *
 * Note: this cache obtains a locks when there is a cache miss.
 */
public class ConcurrentViewCache implements ViewCache {
    private static final int DEFAULT_CONCURRENCY = 1; // identical to ConcurrentHashMap's default

    private final Map<String, View> accessCache;
    private final Map<String, View> creationCache;

    public ConcurrentViewCache(int capacity, int concurrency) {
        this.accessCache = new ConcurrentHashMap<>(capacity, 0.75f, concurrency);
        this.creationCache = new LinkedHashMap<String, View>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, View> eldest) {
                accessCache.remove(eldest.getKey());
                return size() > capacity;
            }
        };
    }

    @Override
    public View getOrCreate(String key, Callable<View> callable) throws Exception {
        View view = accessCache.get(key);
        if (view == null) {
            synchronized (creationCache) {
                view = this.creationCache.get(key);
                if (view == null) {
                    view = callable.call();
                    accessCache.put(key, view);
                    creationCache.put(key, view);
                }
            }
        }
        return view;
    }

    /**
     * Constructs a thread-safe view cache with default capacity and concurrency
     */
    static ViewCache defaultCache() {
        return ConcurrentViewCache.builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int capacity = DEFAULT_CACHE_CAPACITY;
        private int concurrency = DEFAULT_CONCURRENCY;

        public Builder withCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder withConcurrency(int concurrency) {
            this.concurrency = concurrency;
            return this;
        }

        public ConcurrentViewCache build() {
            return new ConcurrentViewCache(capacity, concurrency);
        }
    }
}
