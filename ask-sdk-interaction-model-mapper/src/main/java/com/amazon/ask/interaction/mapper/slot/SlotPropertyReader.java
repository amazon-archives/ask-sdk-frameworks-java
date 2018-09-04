package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.SlotValueParseException;

/**
 *
 */
public interface SlotPropertyReader<T> {
    T read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException;
}
