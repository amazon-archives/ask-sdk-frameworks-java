package com.amazon.ask.models.types.slot.date;

import com.amazon.ask.model.Slot;

import java.util.Objects;

import static com.amazon.ask.models.Utils.checkArgument;

/**
 * Utterances that combined to the weekend for a specific week (such as “this weekend”) convert to a date indicating the
 * week number and weekend: 2015-W49-WE.
 */
public class WeekendDate extends AmazonDate {
    private final int year;
    private final int week;

    public WeekendDate(Slot slot, int year, int week) {
        super(slot);
        checkArgument(year >= 0, "year must be >= 0");
        checkArgument(week >= 0, "week must be >= 0");
        this.year = year;
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        WeekendDate that = (WeekendDate) o;
        return year == that.year && week == that.week;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), year, week);
    }

    @Override
    public String toString() {
        return "WeekendDate{" + "year=" + year + ", week=" + week + ", slot=" + getSlot() + '}';
    }
}
