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
 * Converts words that indicate durations into a numeric duration.
 *
 * The duration is provided to your skill in a format based on the ISO-8601 duration format (PnYnMnDTnHnMnS). The P
 * indicates that this is a duration. The n is the numeric value, and the capital letter following the n designates the
 * specific date or time element. For example, P3D means 3 days. A T is used to indicate that the remaining values
 * represent time elements rather than date elements.
 *
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#duration">AMAZON.DURATION docs</a>
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
