package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Resolutions;

/**
 *
 */
public class ResolutionsReader implements SlotPropertyReader<Resolutions> {
    @Override
    public Resolutions read(IntentRequest intentRequest, Slot slot) {
        return slot.getResolutions();
    }
}
