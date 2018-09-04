package com.amazon.ask.models.types.slot.date;


import com.amazon.ask.model.Slot;

import java.util.Objects;

import static com.amazon.ask.models.Utils.checkArgument;

/**
 * Utterances that combined to a year (such as “next year”) convert to a date containing just the year: 2016.
 */
public class YearDate extends AmazonDate {
    private final int year;

    public YearDate(Slot slot, int year) {
        super(slot);
        checkArgument(year >= 0, "year must be >= 0");
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        YearDate yearDate = (YearDate) o;
        return year == yearDate.year;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), year);
    }

    @Override
    public String toString() {
        return "YearDate{" + "year=" + year + ", slot=" + getSlot() + '}';
    }
}