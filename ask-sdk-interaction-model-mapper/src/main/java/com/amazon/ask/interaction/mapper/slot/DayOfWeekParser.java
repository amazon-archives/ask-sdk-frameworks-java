package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.SlotValueParseException;
import com.amazon.ask.interaction.types.slot.list.DayOfWeek;

import static java.time.DayOfWeek.valueOf;

/**
 *
 */
public class DayOfWeekParser implements SlotPropertyReader<DayOfWeek> {
    @Override
    public DayOfWeek read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        try {
            return new DayOfWeek(slot, valueOf(slot.getValue().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new SlotValueParseException(slot, DayOfWeek.class, ex);
        }
    }
}
