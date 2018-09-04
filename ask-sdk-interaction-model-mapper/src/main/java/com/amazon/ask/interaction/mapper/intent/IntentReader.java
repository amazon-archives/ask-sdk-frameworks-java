package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.interaction.mapper.IntentParseException;

public interface IntentReader<T> {
    /**
     * Parse an IntentRequest to a concrete instance
     *
     * @param intentRequest
     * @return instance representation of this request
     * @throws IntentParseException
     */
    T read(IntentRequest intentRequest) throws IntentParseException;
}
