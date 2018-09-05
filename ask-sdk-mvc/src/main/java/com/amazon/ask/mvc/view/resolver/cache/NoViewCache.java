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
