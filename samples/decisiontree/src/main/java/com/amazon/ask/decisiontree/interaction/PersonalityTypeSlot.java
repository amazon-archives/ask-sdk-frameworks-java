package com.amazon.ask.decisiontree.interaction;

import com.amazon.ask.models.annotation.data.SlotTypeResource;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.types.slot.BaseSlotValue;


@SlotType("personalityType")
@SlotTypeResource("models/personality_type_slot")
public class PersonalityTypeSlot extends BaseSlotValue {
}
