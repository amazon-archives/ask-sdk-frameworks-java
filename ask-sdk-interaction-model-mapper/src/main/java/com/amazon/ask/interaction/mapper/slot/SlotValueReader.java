package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;

/**
 *
 */
public class SlotValueReader implements SlotPropertyReader<String> {
    @Override
    public String read(IntentRequest intentRequest, Slot slot) {
        return slot.getValue();
    }
}
