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
