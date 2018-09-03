package com.amazon.ask.mvc.controller;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;

public class BaseMappingsController {
    @IntentMapping(name = "Base")
    public Response handleEnvelopeBase(RequestEnvelope envelope) {
        return Utils.EMPTY_RESPONSE;
    }
}
