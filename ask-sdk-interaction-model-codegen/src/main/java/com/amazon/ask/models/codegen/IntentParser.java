package com.amazon.ask.models.codegen;

import com.amazon.ask.interaction.model.Intent;
import com.amazon.ask.interaction.model.Slot;
import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.data.model.IntentSlotData;
import com.amazon.ask.models.data.model.SlotTypeData;
import com.amazon.ask.models.definition.IntentDefinition;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.amazon.ask.models.codegen.Utils.isAmazon;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class IntentParser {
    private final SlotTypeParser slotTypeParser;

    public IntentParser(SlotTypeParser slotTypeParser) {
        this.slotTypeParser = assertNotNull(slotTypeParser, "slotTypeParser");
    }

    public Map<IntentDefinition, IntentData> parse(LocalizedInteractionModel model) {
        Map<IntentDefinition, IntentData> results = new HashMap<>();

        Map<String, IntentDefinition> seenIntents = new HashMap<>();

        Map<SlotTypeDefinition, SlotTypeData> slotTypes = slotTypeParser.parse(model);
        for (Intent intent : model.getSkillModel().getInteractionModel().getLanguageModel().getIntents()) {
            IntentDefinition intentDefinition = parseDefinition(intent, slotTypes.keySet());
            if (seenIntents.containsKey(intent.getName())) {
                IntentDefinition other = seenIntents.get(intent.getName());
                if (!intentDefinition.equals(other)) {
                    throw new IllegalArgumentException("Duplicate intent names don't have matching definitions: " + intentDefinition + " does not match " + other);
                }
                seenIntents.put(intent.getName(), intentDefinition);
            }

            IntentData intentData = parseData(intent);
            if (results.containsKey(intentDefinition)) {
                intentData = IntentData.combine(results.get(intentDefinition), intentData);
            }
            results.put(intentDefinition, intentData);
        }
        return results;
    }

    protected IntentDefinition parseDefinition(Intent intent, Collection<SlotTypeDefinition> slotTypes) {
        return IntentDefinition.builder()
            .withName(intent.getName())
            .withIntentType(TypeFactory.defaultInstance().constructSimpleType(Object.class, new JavaType[]{}))
            .withSlots(parseSlotDefinitions(intent, slotTypes))
            .withCustom(!isAmazon(intent.getName()))
            .build();
    }

    protected IntentData parseData(Intent intent) {
        IntentData.Builder builder = IntentData.builder();
        builder.withSamples(intent.getSamples());
        if (intent.getSlots() != null) {
            for (Slot slot : intent.getSlots()) {
                builder.addSlot(slot.getName(), IntentSlotData.builder()
                    .withSamples(slot.getSamples())
                    .build());
            }
        }
        return builder.build();
    }

    protected Map<String, SlotTypeDefinition> parseSlotDefinitions(Intent intent, Collection<SlotTypeDefinition> slotTypes) {
        Map<String, SlotTypeDefinition> slotTypeIndex = slotTypes.stream().collect(Collectors.toMap(
            SlotTypeDefinition::getName,
            Function.identity()));

        Map<String, SlotTypeDefinition> intentSlots = new LinkedHashMap<>();
        if (intent.getSlots() != null) {
            for (Slot slot : intent.getSlots()) {
                SlotTypeDefinition slotType = slotTypeIndex.get(slot.getType());
                if (slotType == null) {
                    if (!isAmazon(slot.getType())) {
                        throw new IllegalArgumentException("Custom Slot type '" + slot.getType() + "' is used on Intent '" + intent.getName() + "' for slot '" + slot.getName() +"' but is not defined in the Interaction Model");
                    } else {
                        slotType = SlotTypeDefinition.builder()
                            .withName(slot.getType())
                            .withCustom(false)
                            .withSlotTypeClass(Object.class)
                            .build();
                    }
                }

                intentSlots.put(slot.getName(), slotType);
            }
        }
        return intentSlots;
    }
}
