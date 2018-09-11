package ${package}.slots;

import com.amazon.ask.interaction.annotation.data.SlotTypeResource;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.types.slot.BaseSlotValue;

@SlotType
@SlotTypeResource("pet_type")
public class PetType extends BaseSlotValue {
}