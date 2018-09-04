package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.SlotValueParseException;
import com.amazon.ask.interaction.types.slot.time.AbsoluteTime;
import com.amazon.ask.interaction.types.slot.time.AmazonTime;
import com.amazon.ask.interaction.types.slot.time.RelativeTime;
import com.amazon.ask.interaction.types.slot.time.TimeOfDay;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 *
 */
public class AmazonTimeParser implements SlotPropertyReader<AmazonTime> {
    @Override
    public AmazonTime read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        String value = slot.getValue();
        try {
            if (value.length() == 2) {
                return new RelativeTime(slot, TimeOfDay.valueOf(value));
            } else {
                return new AbsoluteTime(slot, LocalTime.parse(value));
            }
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            throw new SlotValueParseException(slot, AmazonTime.class, ex);
        }
    }
}
