package ${package}.intents;

import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.annotation.data.SlotProperty;

import ${package}.slots.PetType;

@Intent
@IntentResource("pet_type_intent")
public class PetTypeIntent {
    @SlotProperty
    private PetType petType;

    public PetTypeIntent() {}

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }
}