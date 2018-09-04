package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.mapper.slot.RawSlotPropertyReader;

public abstract class BaseSlotValue {
    @SlotPropertyReader(RawSlotPropertyReader.class)
    private Slot slot;

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseSlotValue that = (BaseSlotValue) o;

        return slot.equals(that.slot);
    }

    @Override
    public int hashCode() {
        return slot.hashCode();
    }
}
