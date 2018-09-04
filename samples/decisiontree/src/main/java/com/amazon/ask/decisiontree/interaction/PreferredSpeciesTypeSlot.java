package com.amazon.ask.decisiontree.interaction;

import com.amazon.ask.models.annotation.data.SlotTypeResource;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.types.slot.BaseSlotValue;


@SlotType("preferredSpeciesType")
@SlotTypeResource("models/preferred_species_type_slot")
public class PreferredSpeciesTypeSlot extends BaseSlotValue {
}