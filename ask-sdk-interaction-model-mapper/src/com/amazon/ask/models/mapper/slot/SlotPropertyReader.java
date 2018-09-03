package com.amazon.ask.models.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.models.mapper.SlotValueParseException;

/**
 *
 */
public interface SlotPropertyReader<T> {
    T read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException;
}
