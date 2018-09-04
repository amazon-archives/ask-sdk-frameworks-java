package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonDurationParser;

import java.time.Duration;
import java.time.Period;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#duration">Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.DURATION")
@SlotPropertyReader(AmazonDurationParser.class)
public class AmazonDuration extends BaseSlotValue {
    private final Period period;
    private final Duration duration;

    public AmazonDuration(Slot slot, Period period, Duration duration) {
        this.setSlot(slot);
        this.period = assertNotNull(period, "period");
        this.duration = assertNotNull(duration, "duration");
    }

    public Period getPeriod() {
        return period;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AmazonDuration that = (AmazonDuration) o;
        return Objects.equals(period, that.period) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), period, duration);
    }

    @Override
    public String toString() {
        return "AmazonDuration{" + "period=" + period + ", duration=" + duration + ", slot=" + getSlot() + '}';
    }
}
