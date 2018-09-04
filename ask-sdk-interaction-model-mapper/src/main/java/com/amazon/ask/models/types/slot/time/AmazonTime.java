package com.amazon.ask.models.types.slot.time;

import com.amazon.ask.models.annotation.data.SlotPropertyReader;
import com.amazon.ask.models.annotation.type.BuiltIn;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.mapper.slot.AmazonTimeParser;
import com.amazon.ask.models.types.slot.BaseSlotValue;

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
