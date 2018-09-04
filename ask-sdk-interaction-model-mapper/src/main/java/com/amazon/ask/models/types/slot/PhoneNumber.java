package com.amazon.ask.models.types.slot;

import com.amazon.ask.models.annotation.type.BuiltIn;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.types.slot.BaseSlotValue;

/**
 * @link https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#phonenumber
 */
@BuiltIn
@SlotType("AMAZON.PhoneNumber")
public class PhoneNumber extends BaseSlotValue {
}
