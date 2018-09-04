package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.IntentRequest;

/**
 * Thrown when parsing an {@link IntentRequest} fails.
 */
public class IntentParseException extends Exception {
    public IntentParseException(String msg) { super(msg); }
    public IntentParseException(String msg, Throwable cause) { super(msg, cause); }
}
