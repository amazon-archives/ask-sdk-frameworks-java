package com.amazon.ask.interaction.renderer.models;

import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.type.Intent;

/**
 *
 */
@Intent("TestPrompts")
@IntentResource("TestPrompts")
public class TestPrompts {
    @SlotProperty
    private TestCustom customSlot;

    public TestCustom getCustomSlot() {
        return customSlot;
    }

    public void setCustomSlot(TestCustom customSlot) {
        this.customSlot = customSlot;
    }
}
