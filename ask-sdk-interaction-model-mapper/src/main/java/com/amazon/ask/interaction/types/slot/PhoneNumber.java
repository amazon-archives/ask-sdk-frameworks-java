package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#phonenumber">Slot Type Reference</a>
 */
@BuiltIn
@SlotType("AMAZON.PhoneNumber")
public class PhoneNumber extends BaseSlotValue {
}
