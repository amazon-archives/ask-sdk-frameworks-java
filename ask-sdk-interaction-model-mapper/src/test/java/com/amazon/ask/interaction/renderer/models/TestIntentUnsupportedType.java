package com.amazon.ask.interaction.renderer.models;

import com.amazon.ask.interaction.annotation.type.Intent;

/**
 *
 */
@Intent("TestIntentUnsupportedType")
public class TestIntentUnsupportedType {
    private Double unsupported;

    public Double getUnsupported() { return unsupported; }
    public void setUnsupported(Double unsupported) { this.unsupported = unsupported; }
}
