package com.amazon.ask.models.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.models.mapper.SlotValueParseException;
import com.amazon.ask.models.types.slot.AmazonDuration;

import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 *
 */
public class AmazonDurationParser implements SlotPropertyReader<AmazonDuration> {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("T");

    @Override
    public AmazonDuration read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        String value = slot.getValue();
        String[] split = SPLIT_PATTERN.split(value, 2);
        try {
            if (split.length == 1) {
                return new AmazonDuration(slot, Period.parse(value), Duration.ZERO);
            } else {
                Period p;
                if (split[0].equals("P")) p = Period.ZERO;
                else p = Period.parse(split[0]);

                return new AmazonDuration(slot, p, Duration.parse("PT" + split[1]));
            }
        } catch (DateTimeParseException ex) {
            throw new SlotValueParseException(slot, AmazonDuration.class, ex);
        }
    }
}
