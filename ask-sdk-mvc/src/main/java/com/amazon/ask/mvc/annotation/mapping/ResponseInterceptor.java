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

import com.amazon.ask.mvc.annotation.plugin.AutoResponseInterceptor;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.mapper.invoke.ResponseInterceptorMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@AutoResponseInterceptor(ResponseInterceptor.Plugin.class)
public @interface ResponseInterceptor {

    /**
     * TODO
     */
    class Plugin implements AutoResponseInterceptor.Plugin<ResponseInterceptor> {
        @Override
        public com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor apply(ControllerMethodContext context, ResponseInterceptor responseInterceptor) {
            return ResponseInterceptorMethod.builder().withContext(context).build();
        }
    }
}
