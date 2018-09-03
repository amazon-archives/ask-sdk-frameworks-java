package com.amazon.ask.colorpicker.interaction;

import com.amazon.ask.models.annotation.data.SlotProperty;
import com.amazon.ask.models.annotation.data.IntentResource;
import com.amazon.ask.models.annotation.type.Intent;

@Intent("MyColorIsIntent")
@IntentResource("models/my_color_is_intent")
public class MyColorIsIntent {

    @SlotProperty
    private ListOfColors color;

    public ListOfColors getColor() {
        return this.color;
    }

    public void setColor(ListOfColors Color) {
        this.color = Color;
    }
}
