package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.SlotValueParseException;
import com.amazon.ask.interaction.types.slot.AmazonNumber;

/**
 *
 */
public class AmazonNumberParser implements SlotPropertyReader<AmazonNumber> {
    @Override
    public AmazonNumber read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        try {
            return new AmazonNumber(slot, Long.valueOf(slot.getValue()));
        } catch (NumberFormatException ex) {
            throw new SlotValueParseException(slot, AmazonNumber.class, ex);
        }
    }
}
