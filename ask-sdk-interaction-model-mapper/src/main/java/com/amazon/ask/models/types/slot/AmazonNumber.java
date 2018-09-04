package com.amazon.ask.models.types.slot;

import com.amazon.ask.models.annotation.data.SlotPropertyReader;
import com.amazon.ask.models.annotation.type.BuiltIn;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.mapper.slot.AmazonNumberParser;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
@BuiltIn
@SlotType(value = "AMAZON.NUMBER")
@SlotPropertyReader(AmazonNumberParser.class)
public class AmazonNumber extends BaseSlotValue {
    private final long number;

    public AmazonNumber(com.amazon.ask.model.Slot slot, long number) {
        this.setSlot(assertNotNull(slot, "slot"));
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AmazonNumber that = (AmazonNumber) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number);
    }

    @Override
    public String toString() {
        return "AmazonNumber{" +
            "number=" + number +
            ", slot=" + getSlot() +
            '}';
    }
}
