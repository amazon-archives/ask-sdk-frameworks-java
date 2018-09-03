package com.amazon.ask.models.mapper;

import com.amazon.ask.model.IntentRequest;

/**
 * Thrown if the IntentRequest's name is unrecognized.
 *
 * @see IntentMapper#parseIntent(IntentRequest)
 */
public class UnrecognizedIntentException extends IntentParseException {
    public UnrecognizedIntentException(IntentRequest intentRequest) {
        super("unrecognized intent: " + intentRequest.getIntent().getName());
    }
}
