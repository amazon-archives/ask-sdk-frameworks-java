package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/literal-slot-type-reference.html">LITERAL Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.LITERAL")
public class AmazonLiteral extends BaseSlotValue {
}
