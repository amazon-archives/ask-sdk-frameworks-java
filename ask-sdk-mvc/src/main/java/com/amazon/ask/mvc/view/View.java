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
