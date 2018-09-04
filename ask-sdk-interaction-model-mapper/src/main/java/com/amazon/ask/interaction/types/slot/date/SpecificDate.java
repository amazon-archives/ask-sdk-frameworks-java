package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;

import java.time.LocalDate;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Utterances that combined to a specific date (such as “today”, “now”, or “november twenty-fifth”) convert to a complete
 * date: 2015-11-25. Note that this defaults to dates on or after the current date (see below for more examples).
 *
 * The date is provided to your service in ISO-8601 date format
 */
public class SpecificDate extends AmazonDate {
    private final LocalDate date;

    public SpecificDate(Slot slot, LocalDate date) {
        super(slot);
        this.date = assertNotNull(date, "date");
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        SpecificDate that = (SpecificDate) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }

    @Override
    public String toString() {
        return "SpecificDate{" + "date=" + date + ", slot=" + getSlot() + '}';
    }
}
