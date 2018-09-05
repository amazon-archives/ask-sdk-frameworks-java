/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.model.Intent;
import com.amazon.ask.interaction.model.Slot;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.IntentSlotData;
import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.amazon.ask.interaction.codegen.Utils.isAmazon;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class IntentParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentParser.class);

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

                String beanName = Introspector.decapitalize(slot.getName());
                if (!beanName.equals(slot.getName())) {
                    LOGGER.warn("Renamed slot from '{}' to '{}' so that it matches the java.beans convention", slot.getName(), beanName);
                }
                if (intentSlots.containsKey(beanName)) {
                    throw new IllegalArgumentException(String.format("Slot '%s' defined twice on intent '%s'", beanName, intent.getName()));
                }

                intentSlots.put(beanName, slotType);
            }
        }
        return intentSlots;
    }
}
