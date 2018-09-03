package com.amazon.ask.models.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;

/**
 *
 */
public class SlotNameReader implements SlotPropertyReader<String> {
    @Override
    public String read(IntentRequest intentRequest, Slot slot) {
        return slot.getName();
    }
}
