package com.amazon.ask.models.mapper;

import com.amazon.ask.model.IntentRequest;

/**
 * Thrown if the IntentRequest does not contain a specific slot
 *
 * @see IntentMapper#parseIntentSlot(IntentRequest, String)
 */
public class UnrecognizedSlotException extends IntentParseException {
    public UnrecognizedSlotException(IntentRequest intentRequest, String slotName) {
        super(String.format("IntentRequest for '%s' is missing slot '%s'", intentRequest.getIntent().getName(), slotName));
    }
}
