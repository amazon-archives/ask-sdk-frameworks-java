package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.slot.AmazonTimeParser;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.amazon.ask.interaction.types.slot.time.AbsoluteTime;
import com.amazon.ask.interaction.types.slot.time.AmazonTime;
import com.amazon.ask.interaction.types.slot.time.RelativeTime;
import com.amazon.ask.interaction.types.slot.time.TimeOfDay;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class AmazonTimeTest {
    private static final SlotPropertyReader<AmazonTime> parser = new AmazonTimeParser();
    private static Slot slot(String value) {
        return Slot.builder().withName("test").withValue(value).build();
    }

    @Test
    public void testRelativeTime() throws IntentParseException {
        assertEquals(new RelativeTime(slot("AF"), TimeOfDay.AF), parser.read(null, slot("AF")));
        assertEquals(new RelativeTime(slot("EV"), TimeOfDay.EV), parser.read(null, slot("EV")));
        assertEquals(new RelativeTime(slot("MO"), TimeOfDay.MO), parser.read(null, slot("MO")));
        assertEquals(new RelativeTime(slot("NI"), TimeOfDay.NI), parser.read(null, slot("NI")));
    }

    @Test(expected = IntentParseException.class)
    public void testRelativeTimeInvalid() throws IntentParseException {
        parser.read(null, slot("AA"));
    }

    @Test
    public void testAbsoluteTime() throws IntentParseException {
        assertEquals(new AbsoluteTime(slot("00:00"), LocalTime.of(0, 0)), parser.read(null, slot("00:00")));
        assertEquals(new AbsoluteTime(slot("23:59"), LocalTime.of(23, 59)), parser.read(null, slot("23:59")));
    }

    @Test(expected = IntentParseException.class)
    public void testAbsoluteTimeOutOfBounds() throws IntentParseException {
        parser.read(null, slot("24:00"));
    }

    @Test(expected = IntentParseException.class)
    public void testAbsoluteTimeInvalid() throws IntentParseException {
        parser.read(null, slot("invalid"));
    }
}
