package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PresentRefTest {
    private final LocalDateTime mockTime1 = LocalDateTime.of(2018, 1, 1, 0, 0);
    private final LocalDateTime mockTime2 = LocalDateTime.of(2018, 1, 1, 0, 1);

    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new PresentRef(null, mockTime1);
    }

    @Test
    public void testTime() {
        new PresentRef(mockSlot, mockTime1);
        new PresentRef(mockSlot, mockTime2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTime() {
        new PresentRef(mockSlot, null);
    }

    @Test
    public void testEqualsSelf() {
        PresentRef Time = new PresentRef(mockSlot, mockTime1);

        assertEquals(Time, Time);
        assertEquals(Time.getTime(), Time.getTime());
        assertEquals(Time.getSlot(), Time.getSlot());
        assertEquals(Time.hashCode(), Time.hashCode());
        assertEquals(Time.toString(), Time.toString());
    }

    @Test
    public void testEquals() {
        PresentRef Time1 = new PresentRef(mockSlot, mockTime1);
        PresentRef Time2 = new PresentRef(mockSlot, mockTime1);

        assertEquals(Time1, Time2);
        assertEquals(Time1.getTime(), Time2.getTime());
        assertEquals(Time1.getSlot(), Time2.getSlot());
        assertEquals(Time1.hashCode(), Time2.hashCode());
        assertEquals(Time1.toString(), Time2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        PresentRef Time1 = new PresentRef(mockSlot, mockTime1);

        assertNotEquals(Time1, null);
        assertNotEquals(null, Time1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        PresentRef Time1 = new PresentRef(mockSlot, mockTime1);

        assertNotEquals(Time1, "different");
        assertNotEquals("different", Time1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        PresentRef Time1 = new PresentRef(mockSlot, mockTime1);
        PresentRef Time2 = new PresentRef(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), mockTime1);

        assertNotEquals(Time1, Time2);
        assertEquals(Time1.getTime(), Time2.getTime());
        assertNotEquals(Time1.getSlot(), Time2.getSlot());
        assertNotEquals(Time1.hashCode(), Time2.hashCode());
        assertNotEquals(Time1.toString(), Time2.toString());
    }

    @Test
    public void testNotEqualsDifferentTime() {
        PresentRef Time1 = new PresentRef(mockSlot, mockTime1);
        PresentRef Time2 = new PresentRef(mockSlot, mockTime2);

        assertNotEquals(Time1, Time2);
        assertNotEquals(Time1.getTime(), Time2.getTime());
        assertEquals(Time1.getSlot(), Time2.getSlot());
        assertNotEquals(Time1.hashCode(), Time2.hashCode());
        assertNotEquals(Time1.toString(), Time2.toString());
    }
}
