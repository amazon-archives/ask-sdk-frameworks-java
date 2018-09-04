package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;

/**
 *
 */
public class SlotConfirmationStatusReader implements SlotPropertyReader<SlotConfirmationStatus> {
    @Override
    public SlotConfirmationStatus read(IntentRequest intentRequest, Slot slot) {
        return slot.getConfirmationStatus();
    }
}
