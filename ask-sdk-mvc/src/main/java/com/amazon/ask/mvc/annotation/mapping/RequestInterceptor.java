/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.annotation.mapping;

import com.amazon.ask.mvc.annotation.plugin.AutoRequestInterceptor;
import com.amazon.ask.mvc.mapper.invoke.RequestInterceptorMethod;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register a controller's method as a {@link com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@AutoRequestInterceptor(RequestInterceptor.Plugin.class)
public @interface RequestInterceptor {

    class Plugin implements AutoRequestInterceptor.Plugin<RequestInterceptor> {
        @Override
        public com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor apply(ControllerMethodContext context, RequestInterceptor annotation) {
            return RequestInterceptorMethod.builder().withContext(context).build();
        }
    }
}
