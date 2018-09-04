package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.Slot;

/**
 * Thrown if a slot value does not match the expected format.
 *
 * Maintains the received slot and expected type.
 */
public class SlotValueParseException extends IntentParseException {
    private final Slot slot;
    private final String type;

    public SlotValueParseException(String message, Exception cause, Slot slot, String type) {
        super(message, cause);
        this.slot = slot;
        this.type = type;
    }

    public SlotValueParseException(Slot slot, Class type, Exception cause) {
        this(String.format("invalid '%s' slot value: '%s' = '%s'", type, slot.getName(), slot.getValue()), cause, slot, type.getName());
    }

    public SlotValueParseException(Slot slot, Class type) {
        this(slot, type, null);
    }

    public Slot getSlot() {
        return slot;
    }

    public String getType() {
        return type;
    }
}
