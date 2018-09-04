package com.amazon.ask.models.types.slot.date;

import com.amazon.ask.model.Slot;

import java.util.Objects;

import static com.amazon.ask.models.Utils.checkArgument;
import static com.amazon.ask.util.ValidationUtils.assertIsPositive;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Utterances that combined to a season (such as “next winter”) convert to a date add the year and a season indicator:
 *
 * winter: WI, spring: SP, summer: SU, fall: FA)
 */
public class SeasonDate extends AmazonDate {
    private final int year;
    private final Season season;

    public SeasonDate(Slot slot, int year, Season season) {
        super(slot);
        checkArgument(year >= 0, "year must be >= 0");
        this.year = year;
        this.season = assertNotNull(season, "season");
    }

    public int getYear() {
        return this.year;
    }

    public Season getSeason() {
        return this.season;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        SeasonDate that = (SeasonDate) o;
        return year == that.year && season == that.season;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), year, season);
    }

    @Override
    public String toString() {
        return "SeasonDate{" + "year=" + year + ", season=" + season + ", slot=" + getSlot() + '}';
    }
}
