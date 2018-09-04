package com.amazon.ask.interaction.types.slot.time;

import com.amazon.ask.model.Slot;

import java.time.LocalTime;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Values that represent a specific time are provided to your skill in ISO-8601 time format. Midnight is
 * represented as 00:00
 */
public class AbsoluteTime extends AmazonTime {
    private final LocalTime time;

    public AbsoluteTime(Slot slot, LocalTime time) {
        super(slot);
        this.time = assertNotNull(time, "time");
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AbsoluteTime that = (AbsoluteTime) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), time);
    }

    @Override
    public String toString() {
        return "AbsoluteTime{" +
            "time=" + time +
            "slot=" + getSlot() +
            '}';
    }
}
