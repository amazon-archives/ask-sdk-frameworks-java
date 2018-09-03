package com.amazon.ask.models.types.slot;

import com.amazon.ask.model.Slot;
import com.amazon.ask.models.types.slot.AmazonDuration;
import org.junit.Test;

import java.time.Duration;
import java.time.Period;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 */
public class AmazonDurationTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullPeriod() {
        new AmazonDuration(
            mockSlot,
            null,
            Duration.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDuration() {
        new AmazonDuration(
            mockSlot,
            Period.ZERO,
            null);
    }

    @Test
    public void testEqualsSelf() {
        AmazonDuration test = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ZERO);

        assertEquals(test, test);
        assertEquals(test.hashCode(), test.hashCode());
    }

    @Test
    public void testNotEqualsNull() {
        AmazonDuration test = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ZERO);

        assertNotEquals(test, null);
        assertNotEquals(null, test);
    }

    @Test
    public void testNotEqualsWrongClass() {
        AmazonDuration test = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ZERO);

        assertNotEquals(test, "test");
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        AmazonDuration left = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ZERO);

        AmazonDuration right = new AmazonDuration(
            Slot.builder()
                .withName("test")
                .withValue("different")
                .build(),
            Period.ZERO,
            Duration.ZERO);

        assertNotEquals(left, right);
        assertEquals(left.getPeriod(), right.getPeriod());
        assertEquals(left.getDuration(), right.getDuration());
        assertNotEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }

    @Test
    public void testNotEqualsDifferentPeriod() {
        AmazonDuration left = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ZERO);

        AmazonDuration right = new AmazonDuration(
            mockSlot,
            Period.ofDays(1),
            Duration.ZERO);

        assertNotEquals(left, right);
        assertNotEquals(left.getPeriod(), right.getPeriod());
        assertEquals(left.getDuration(), right.getDuration());
        assertEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }

    @Test
    public void testNotEqualsDifferentDuration() {
        AmazonDuration left = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ZERO);

        AmazonDuration right = new AmazonDuration(
            mockSlot,
            Period.ZERO,
            Duration.ofHours(1));

        assertNotEquals(left, right);
        assertEquals(left.getPeriod(), right.getPeriod());
        assertNotEquals(left.getDuration(), right.getDuration());
        assertEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }
}
