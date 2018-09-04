package com.amazon.ask.interaction.stubs;

import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;

/**
 *
 */
@Intent
public class TestGenericIntent<T> {
    @SlotProperty
    private T slot;

    public T getSlot() {
        return slot;
    }

    public void setSlot(T slot) {
        this.slot = slot;
    }
}
