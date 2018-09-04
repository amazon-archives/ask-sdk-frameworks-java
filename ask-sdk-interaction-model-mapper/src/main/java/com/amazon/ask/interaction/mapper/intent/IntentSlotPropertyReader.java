package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.interaction.mapper.IntentParseException;
import com.amazon.ask.interaction.mapper.UnrecognizedSlotException;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.fasterxml.jackson.databind.JavaType;

import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;
import static com.amazon.ask.util.ValidationUtils.assertStringNotEmpty;

/**
 * Reads an intent's property using a delegate {@link SlotPropertyReader}
 *
 * This is the case when an intent's property is annotated with {@link SlotProperty}
 *
 * @see IntentMapper#intentReaderFor(JavaType)
 */
public class IntentSlotPropertyReader<T> implements IntentPropertyReader<T> {
    private final SlotPropertyReader<T> slotPropertyReader;
    private final String slotName;

    public IntentSlotPropertyReader(SlotPropertyReader<T> slotPropertyReader, String slotName) {
        this.slotPropertyReader = assertNotNull(slotPropertyReader, "slotPropertyReader");
        this.slotName = assertStringNotEmpty(slotName, "slotName");
    }

    @Override
    public T read(IntentPropertyContext context) throws IntentParseException {
        Map<String, Slot> slots = context.getIntentRequest().getIntent().getSlots();
        if (!slots.containsKey(slotName)) {
            throw new UnrecognizedSlotException(context.getIntentRequest(), slotName);
        }
        Slot slot = slots.get(slotName);
        if (slot == null || slot.getValue() == null) {
            return null;
        }

        return slotPropertyReader.read(context.getIntentRequest(), slot);
    }
}
