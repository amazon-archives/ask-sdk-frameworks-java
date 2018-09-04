package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.interaction.mapper.IntentParseException;

public interface IntentReader<T> {
    /**
     * Parse an IntentRequest to a concrete instance
     *
     * @param intentRequest intent request
     * @return instance representation of this request
     * @throws IntentParseException if a {@link T} instance could not be read from the request
     */
    T read(IntentRequest intentRequest) throws IntentParseException;
}
