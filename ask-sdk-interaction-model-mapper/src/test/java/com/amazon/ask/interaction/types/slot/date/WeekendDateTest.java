package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WeekendDateTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new WeekendDate(null, 1, 1);
    }

    @Test
    public void testWeek() {
        new WeekendDate(mockSlot, 0, 0);
        new WeekendDate(mockSlot, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeek() {
        new WeekendDate(mockSlot, 1, -1);
    }

    @Test
    public void testYear() {
        new WeekendDate(mockSlot, 0, 1);
        new WeekendDate(mockSlot, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeYear() {
        new WeekendDate(mockSlot, -1, 1);
    }

    @Test
    public void testEqualsSelf() {
        WeekendDate date = new WeekendDate(mockSlot, 1, 1);

        assertEquals(date, date);
        assertEquals(date.getWeek(), date.getWeek());
        assertEquals(date.getYear(), date.getYear());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        WeekendDate date1 = new WeekendDate(mockSlot, 1, 1);
        WeekendDate date2 = new WeekendDate(mockSlot, 1, 1);

        assertEquals(date1, date2);
        assertEquals(date1.getWeek(), date2.getWeek());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        WeekendDate date1 = new WeekendDate(mockSlot, 1, 1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        WeekendDate date1 = new WeekendDate(mockSlot, 1, 1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        WeekendDate date1 = new WeekendDate(mockSlot, 1, 1);
        WeekendDate date2 = new WeekendDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getWeek(), date2.getWeek());
        assertEquals(date1.getYear(), date2.getYear());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentYear() {
        WeekendDate date1 = new WeekendDate(mockSlot, 1, 1);
        WeekendDate date2 = new WeekendDate(mockSlot, 2, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getWeek(), date2.getWeek());
        assertNotEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentWeek() {
        WeekendDate date1 = new WeekendDate(mockSlot, 1, 1);
        WeekendDate date2 = new WeekendDate(mockSlot, 1, 2);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getWeek(), date2.getWeek());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
