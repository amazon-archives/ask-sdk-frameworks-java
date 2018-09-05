/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;

/**
 * Renders the response for a request, given a view model.
 */
public interface View {
    /**
     * Render the response for a request.
     *
     * @param handlerOutput the model for the response
     * @param requestEnvelope the envelope for the current request
     * @return rendered response from the view
     * @throws Exception if there was an error rendering the response
     */
    Response render(Object handlerOutput, RequestEnvelope requestEnvelope) throws Exception;
}
