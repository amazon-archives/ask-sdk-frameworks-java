package com.amazon.ask.interaction.types.slot.time;

import com.amazon.ask.model.Slot;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Note that this slot also supports time-related utterances that combined to a time period such as “evening”. This type
 * of utterance returns a time period indicator instead of an ISO-8601 formatted time. For example, “evening”
 * returns the value EV. The following time period indicators can be returned: night: NI, morning: MO,
 * afternoon: AF, evening: EV.
 */
public class RelativeTime extends AmazonTime {
    private final TimeOfDay time;

    public RelativeTime(Slot slot, TimeOfDay time) {
        super(slot);
        this.time = assertNotNull(time, "time");
    }

    public TimeOfDay getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        RelativeTime that = (RelativeTime) o;
        return time == that.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), time);
    }

    @Override
    public String toString() {
        return "RelativeTime{" +
            "time=" + time +
            "slot=" + getSlot() +
            '}';
    }
}
