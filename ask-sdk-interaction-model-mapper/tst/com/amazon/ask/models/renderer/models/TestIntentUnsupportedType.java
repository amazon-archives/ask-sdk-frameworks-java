package com.amazon.ask.models.renderer.models;

import com.amazon.ask.models.annotation.type.Intent;

/**
 *
 */
@Intent("TestIntentUnsupportedType")
public class TestIntentUnsupportedType {
    private Double unsupported;

    public Double getUnsupported() { return unsupported; }
    public void setUnsupported(Double unsupported) { this.unsupported = unsupported; }
}
