package com.amazon.ask.models.stubs;

import com.amazon.ask.models.annotation.data.SlotProperty;
import com.amazon.ask.models.annotation.type.Intent;

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
