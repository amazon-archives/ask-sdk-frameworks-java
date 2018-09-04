package com.amazon.ask.models.stubs;

import com.amazon.ask.models.annotation.data.SlotProperty;
import com.amazon.ask.models.annotation.type.Intent;

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
