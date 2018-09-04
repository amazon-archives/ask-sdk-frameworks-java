package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * TODO: Can't find docs for this anymore?
 */
public class PresentRef extends AmazonDate {
    private final LocalDateTime time;

    public PresentRef(Slot slot, LocalDateTime time) {
        super(slot);
        this.time = assertNotNull(time, "time");
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        PresentRef that = (PresentRef) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), time);
    }

    @Override
    public String toString() {
        return "PresentRef{" + "time=" + time + ", slot=" + getSlot() + '}';
    }
}
