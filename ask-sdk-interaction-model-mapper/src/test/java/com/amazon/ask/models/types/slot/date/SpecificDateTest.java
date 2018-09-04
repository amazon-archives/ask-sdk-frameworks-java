package com.amazon.ask.models.types.slot.date;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SpecificDateTest {
    private final LocalDate mockDate1 = LocalDate.of(2018, 1, 1);
    private final LocalDate mockDate2 = LocalDate.of(2018, 1, 2);

    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new SpecificDate(null, mockDate1);
    }

    @Test
    public void testDate() {
        new SpecificDate(mockSlot, mockDate1);
        new SpecificDate(mockSlot, mockDate2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDate() {
        new SpecificDate(mockSlot, null);
    }

    @Test
    public void testEqualsSelf() {
        SpecificDate date = new SpecificDate(mockSlot, mockDate1);

        assertEquals(date, date);
        assertEquals(date.getDate(), date.getDate());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        SpecificDate date1 = new SpecificDate(mockSlot, mockDate1);
        SpecificDate date2 = new SpecificDate(mockSlot, mockDate1);

        assertEquals(date1, date2);
        assertEquals(date1.getDate(), date2.getDate());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        SpecificDate date1 = new SpecificDate(mockSlot, mockDate1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        SpecificDate date1 = new SpecificDate(mockSlot, mockDate1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        SpecificDate date1 = new SpecificDate(mockSlot, mockDate1);
        SpecificDate date2 = new SpecificDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), mockDate1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getDate(), date2.getDate());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentDate() {
        SpecificDate date1 = new SpecificDate(mockSlot, mockDate1);
        SpecificDate date2 = new SpecificDate(mockSlot, mockDate2);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getDate(), date2.getDate());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
