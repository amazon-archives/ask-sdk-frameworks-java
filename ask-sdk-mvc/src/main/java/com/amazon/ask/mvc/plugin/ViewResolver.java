/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.plugin;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.view.View;

import java.util.Optional;

/**
 * Resolves a view to render a request's response.
 */
public interface ViewResolver {

    /**
     * Resolve a view for this request.
     *
     * @param handlerOutput object returned by the request handler
     * @param requestEnvelope the envelope for the current request
     * @return the response to be sent back
     * @throws Exception if there was an error loading/resolving the view
     */
    Optional<View> resolve(Object handlerOutput, RequestEnvelope requestEnvelope) throws Exception;
}
