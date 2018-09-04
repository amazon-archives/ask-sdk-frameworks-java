package com.amazon.ask.interaction.types.slot.list;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 */
public class DayOfWeekTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test
    public void testEqualsSelf() {
        DayOfWeek test = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);

        assertEquals(test, test);
        assertEquals(test.getValue(), test.getValue());
        assertEquals(test.getSlot(), test.getSlot());
        assertEquals(test.hashCode(), test.hashCode());
        assertEquals(test.toString(), test.toString());
    }

    @Test
    public void testEquals() {
        DayOfWeek left = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);
        DayOfWeek right = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);

        assertEquals(left, right);
        assertEquals(left.getValue(), right.getValue());
        assertEquals(left.getSlot(), right.getSlot());
        assertEquals(left.hashCode(), right.hashCode());
        assertEquals(left.toString(), right.toString());
    }

    @Test
    public void testNotEqualsNull() {
        DayOfWeek test = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);
        assertNotEquals(test, null);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        DayOfWeek test = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);
        assertNotEquals(test, "test");
        assertNotEquals("test", test);
    }

    @Test
    public void testNotEqualsDifferentTime() {
        DayOfWeek left = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);
        DayOfWeek right = new DayOfWeek(mockSlot, java.time.DayOfWeek.TUESDAY);

        assertNotEquals(left, right);
        assertNotEquals(left.getValue(), right.getValue());
        assertEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        DayOfWeek left = new DayOfWeek(mockSlot, java.time.DayOfWeek.MONDAY);
        DayOfWeek right = new DayOfWeek(
            Slot.builder()
                .withName("test")
                .withValue("different")
                .build(),
            java.time.DayOfWeek.MONDAY);

        assertNotEquals(left, right);
        assertEquals(left.getValue(), right.getValue());
        assertNotEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }
}
