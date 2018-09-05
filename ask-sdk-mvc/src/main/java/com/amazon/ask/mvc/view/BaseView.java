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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Appends the request envelope to the mode, renders the response to a JSON string and parses the result.
 */
public abstract class BaseView implements View {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ObjectMapper mapper;

    protected BaseView(ObjectMapper mapper) {
        this.mapper = assertNotNull(mapper, "mapper");
    }

    @Override
    public Response render(Object mav, RequestEnvelope requestEnvelope) throws Exception {
        return render(prepareModel((ModelAndView) mav, requestEnvelope));
    }

    protected Response render(Map<String, Object> model) throws Exception {
        return mapper.readValue(renderInternal(model), Response.class);
    }

    protected abstract String renderInternal(Map<String, Object> model) throws Exception;

    protected Map<String, Object> prepareModel(ModelAndView mav, RequestEnvelope requestEnvelope) {
        Map<String, Object> model = new HashMap<>(mav.getModel());
        model.put("envelope", requestEnvelope);
        model.put("request", requestEnvelope.getRequest());
        model.put("session", requestEnvelope.getSession());
        return model;
    }

}
