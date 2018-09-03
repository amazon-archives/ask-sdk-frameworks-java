package com.amazon.ask.models.types.slot.date;


import com.amazon.ask.model.Slot;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertIsPositive;

/**
 * Utterances that combined to a decade convert to a date indicating the decade: 201X.
 */
public class DecadeDate extends AmazonDate {
    private final int century;
    private final int decade;

    public DecadeDate(Slot slot, int century, int decade) {
        super(slot);
        this.century = assertIsPositive(century, "century");
        if (decade < 0) {
            throw new IllegalArgumentException("decade must be zero or positive");
        }
        this.decade = decade;
    }

    public int getCentury() {
        return century;
    }

    public int getDecade() {
        return decade;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        DecadeDate that = (DecadeDate) o;
        return century == that.century && decade == that.decade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), century, decade);
    }

    @Override
    public String toString() {
        return "DecadeDate{" + "century=" + century + ", decade=" + decade + ", slot=" + getSlot() + '}';
    }
}
