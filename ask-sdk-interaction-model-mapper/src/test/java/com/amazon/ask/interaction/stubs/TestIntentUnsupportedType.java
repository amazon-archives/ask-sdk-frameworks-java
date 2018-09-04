package com.amazon.ask.interaction.stubs;

import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;

/**
 *
 */
@Intent(value = "TestIntentUnsupportedType")
public class TestIntentUnsupportedType {
    @SlotProperty
    private Double unsupported;

    public Double getUnsupported() { return unsupported; }
    public void setUnsupported(Double unsupported) { this.unsupported = unsupported; }
}
