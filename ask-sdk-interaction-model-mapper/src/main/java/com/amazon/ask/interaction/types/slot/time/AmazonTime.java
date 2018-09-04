package com.amazon.ask.interaction.types.slot.time;

import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonTimeParser;
import com.amazon.ask.interaction.types.slot.BaseSlotValue;

/**
 * Converts words that indicate time into time values.
 *
 * @see RelativeTime
 * @see AbsoluteTime
 *
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#time">AMAZON.TIME docs</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.TIME")
@SlotPropertyReader(AmazonTimeParser.class)
public abstract class AmazonTime extends BaseSlotValue {
    AmazonTime(com.amazon.ask.model.Slot slot) {
        this.setSlot(slot);
    }
}
