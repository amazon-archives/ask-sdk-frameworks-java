package com.amazon.ask.models.types.slot.date;

import com.amazon.ask.models.annotation.data.SlotPropertyReader;
import com.amazon.ask.models.annotation.type.BuiltIn;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.mapper.slot.AmazonDateParser;
import com.amazon.ask.models.types.slot.BaseSlotValue;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Converts words that represent dates into a date format.
 *
 * The date is provided to your service in ISO-8601 date format. Note that the date your service receives in the slot
 * can vary depending on the specific phrase uttered by the user.
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
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#date">AMAZON.DATE docs</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.DATE")
@SlotPropertyReader(AmazonDateParser.class)
public abstract class AmazonDate extends BaseSlotValue {
    AmazonDate(com.amazon.ask.model.Slot slot) {
        this.setSlot(assertNotNull(slot, "slot"));
    }
}
