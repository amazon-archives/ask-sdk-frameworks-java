package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;

/**
 *
 */
public class RawSlotPropertyReader implements SlotPropertyReader<Slot> {
    @Override
    public Slot read(IntentRequest intentRequest, Slot slot) {
        return slot;
    }
}
