package com.amazon.ask.interaction.types.slot.list;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.DayOfWeekParser;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#list-types">Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.DayOfWeek")
@SlotPropertyReader(DayOfWeekParser.class)
public class DayOfWeek {
    private final Slot slot;
    private final java.time.DayOfWeek value;

    public DayOfWeek(Slot slot, java.time.DayOfWeek value) {
        this.slot = assertNotNull(slot, "slot");
        this.value = assertNotNull(value, "value");
    }

    public Slot getSlot() {
        return slot;
    }

    public java.time.DayOfWeek getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayOfWeek dayOfWeek = (DayOfWeek) o;
        return Objects.equals(slot, dayOfWeek.slot) &&
            value == dayOfWeek.value;
    }

    @Override
    public int hashCode() {

        return Objects.hash(slot, value);
    }

    @Override
    public String toString() {
        return "DayOfWeek{" +
            "slot=" + slot +
            ", value=" + value +
            '}';
    }
}
