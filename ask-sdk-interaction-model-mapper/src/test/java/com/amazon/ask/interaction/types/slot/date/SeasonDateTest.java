package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SeasonDateTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new SeasonDate(null, 1, Season.SU);
    }

    @Test
    public void testSeason() {
        new SeasonDate(mockSlot, 0, Season.SU);
        new SeasonDate(mockSlot, 0, Season.SP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSeason() {
        new SeasonDate(mockSlot, 1, null);
    }

    @Test
    public void testYear() {
        new SeasonDate(mockSlot, 0, Season.SU);
        new SeasonDate(mockSlot, 1, Season.SU);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeYear() {
        new SeasonDate(mockSlot, -1, Season.SU);
    }

    @Test
    public void testEqualsSelf() {
        SeasonDate date = new SeasonDate(mockSlot, 1, Season.SU);

        assertEquals(date, date);
        assertEquals(date.getSeason(), date.getSeason());
        assertEquals(date.getYear(), date.getYear());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        SeasonDate date1 = new SeasonDate(mockSlot, 1, Season.SU);
        SeasonDate date2 = new SeasonDate(mockSlot, 1, Season.SU);

        assertEquals(date1, date2);
        assertEquals(date1.getSeason(), date2.getSeason());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        SeasonDate date1 = new SeasonDate(mockSlot, 1, Season.SU);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        SeasonDate date1 = new SeasonDate(mockSlot, 1, Season.SU);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        SeasonDate date1 = new SeasonDate(mockSlot, 1, Season.SU);
        SeasonDate date2 = new SeasonDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1, Season.SU);

        assertNotEquals(date1, date2);
        assertEquals(date1.getSeason(), date2.getSeason());
        assertEquals(date1.getYear(), date2.getYear());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentYear() {
        SeasonDate date1 = new SeasonDate(mockSlot, 1, Season.SU);
        SeasonDate date2 = new SeasonDate(mockSlot, 2, Season.SU);

        assertNotEquals(date1, date2);
        assertEquals(date1.getSeason(), date2.getSeason());
        assertNotEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentSeason() {
        SeasonDate date1 = new SeasonDate(mockSlot, 1, Season.SU);
        SeasonDate date2 = new SeasonDate(mockSlot, 1, Season.SP);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getSeason(), date2.getSeason());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
