package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonNumberParser;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#number">Slot Type Reference</a>
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
