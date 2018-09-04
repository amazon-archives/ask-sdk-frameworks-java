package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonDateParser;
import com.amazon.ask.interaction.types.slot.BaseSlotValue;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Converts words that represent dates into a date format.
 *
 * @see PresentRef
 * @see DecadeDate
 * @see MonthDate
 * @see SeasonDate
 * @see SpecificDate
 * @see WeekDate
 * @see WeekendDate
 * @see YearDate
 * @see AmazonDateParser
 *
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#date">Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.DATE")
@SlotPropertyReader(AmazonDateParser.class)
public abstract class AmazonDate extends BaseSlotValue {
    AmazonDate(com.amazon.ask.model.Slot slot) {
        this.setSlot(assertNotNull(slot, "slot"));
    }
}
