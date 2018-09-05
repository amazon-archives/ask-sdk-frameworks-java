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
