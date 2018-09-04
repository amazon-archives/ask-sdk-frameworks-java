package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DecadeDateTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new DecadeDate(null, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDecade() {
        new DecadeDate(mockSlot, 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroCentury() {
        new DecadeDate(mockSlot, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCentury() {
        new DecadeDate(mockSlot, -1, 1);
    }

    @Test
    public void testEqualsSelf() {
        DecadeDate date = new DecadeDate(mockSlot, 1, 1);

        assertEquals(date, date);
        assertEquals(date.getCentury(), date.getCentury());
        assertEquals(date.getDecade(), date.getDecade());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        DecadeDate date1 = new DecadeDate(mockSlot, 1, 1);
        DecadeDate date2 = new DecadeDate(mockSlot, 1, 1);

        assertEquals(date1, date2);
        assertEquals(date1.getCentury(), date2.getCentury());
        assertEquals(date1.getDecade(), date2.getDecade());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        DecadeDate date1 = new DecadeDate(mockSlot, 1, 1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        DecadeDate date1 = new DecadeDate(mockSlot, 1, 1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        DecadeDate date1 = new DecadeDate(mockSlot, 1, 1);
        DecadeDate date2 = new DecadeDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getCentury(), date2.getCentury());
        assertEquals(date1.getDecade(), date2.getDecade());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentCentury() {
        DecadeDate date1 = new DecadeDate(mockSlot, 1, 1);
        DecadeDate date2 = new DecadeDate(mockSlot, 2, 1);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getCentury(), date2.getCentury());
        assertEquals(date1.getDecade(), date2.getDecade());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentDecade() {
        DecadeDate date1 = new DecadeDate(mockSlot, 1, 1);
        DecadeDate date2 = new DecadeDate(mockSlot, 1, 2);

        assertNotEquals(date1, date2);
        assertEquals(date1.getCentury(), date2.getCentury());
        assertNotEquals(date1.getDecade(), date2.getDecade());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
