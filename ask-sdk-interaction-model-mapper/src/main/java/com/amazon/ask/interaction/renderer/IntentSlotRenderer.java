/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.*;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.IntentSlotData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;
import static com.amazon.ask.util.ValidationUtils.assertStringNotEmpty;

/**
 * Renders interaction model components for Intent Slots
 */
public class IntentSlotRenderer {
    private static final String CONFIRM_PREFIX = "Confirm";
    private static final String ELICIT_PREFIX = "Elicit";

    /**
     * Renders a slot
     *
     * @param slotName name of the slot on the intent
     * @param slotType slot type definition
     * @param intentData data of owning intent
     * @return renderer slot
     */
    public Slot renderSlot(String slotName, SlotTypeDefinition slotType, IntentData intentData) {
        IntentSlotData data = intentData.getSlots().get(slotName);
        List<String> samples = null;
        if (data != null && !data.getSamples().isEmpty()) {
            samples = new ArrayList<>(data.getSamples());
        }
        return Slot.builder()
            .withName(slotName)
            .withType(slotType.getName())
            .withSamples(samples)
            .build();
    }

    /**
     * Renders a slot's dialog interaction model
     *
     * @param intentDefinition definition of intent to render
     * @param slotName name of the slot on the intent
     * @param slotType slot type definition
     * @param slotData slot type data
     * @return the slot's dialog model
     */
    public DialogSlot renderDialogSlot(IntentDefinition intentDefinition, String slotName, SlotTypeDefinition slotType, IntentSlotData slotData) {
        assertNotNull(intentDefinition, "intentDefinition");
        assertStringNotEmpty(slotName, "slotName");
        assertNotNull(slotType, "slotType");
        assertNotNull(slotData, "slotData");

        return DialogSlot.builder()
            .withName(slotName)
            .withType(slotType.getName())
            .withElicitationRequired(slotData.getElicitationRequired())
            .withConfirmationRequired(slotData.getConfirmationRequired())
            .withPrompts(DialogSlotPrompt.builder()
                .withConfirmation(getConfirmPromptName(intentDefinition, slotName, slotData))
                .withElicitation(getElicitPromptName(intentDefinition, slotName, slotData))
                .build())
            .build();
    }

    /**
     * Renders a slot's dialog prompts.
     *
     * @param intentDefinition definition of intent to render
     * @param slotName name of the slot (on the intent)
     * @param slotData slot type data
     * @return list of the slot's prompts
     */
    public List<Prompt> renderSlotPrompts(IntentDefinition intentDefinition, String slotName, IntentSlotData slotData) {
        assertNotNull(intentDefinition, "intentDefinition");
        assertNotNull(slotName, "slotName");
        assertNotNull(slotData, "slotData");

        List<Prompt> prompts = new ArrayList<>();
        if (!slotData.getConfirmations().isEmpty()) {
            String promptName = getConfirmPromptName(intentDefinition, slotName, slotData);
            prompts.add(getPrompt(promptName, slotData.getConfirmations()));
        }
        if (!slotData.getElicitations().isEmpty()) {
            String promptName = getElicitPromptName(intentDefinition, slotName, slotData);
            prompts.add(getPrompt(promptName, slotData.getElicitations()));
        }
        return prompts;
    }

    protected String getConfirmPromptName(IntentDefinition intentDefinition, String slotName, IntentSlotData slotData) {
        return getPromptName(intentDefinition, slotName, slotData, IntentSlotData::getConfirmations, DialogSlotPrompt::getConfirmation, CONFIRM_PREFIX);

    }

    protected String getElicitPromptName(IntentDefinition intentDefinition, String slotName, IntentSlotData slotData) {
        return getPromptName(intentDefinition, slotName, slotData, IntentSlotData::getElicitations, DialogSlotPrompt::getElicitation, ELICIT_PREFIX);
    }

    protected String getPromptName(IntentDefinition intentDefinition, String slotName, IntentSlotData slotData,
                                   Function<IntentSlotData, Set<PromptVariation>> getVariations, Function<DialogSlotPrompt, String> getPromptId,
                                   String prefix) {
        if (getVariations.apply(slotData).isEmpty()) {
            return null;
        }
        return Optional
            .ofNullable(slotData.getPrompts())
            .map(getPromptId)
            .orElse(String.format("%s.Intent-%s.IntentSlot-%s", prefix, intentDefinition.getName(), slotName));
    }

    private static Prompt getPrompt(String name, Set<PromptVariation> values) {
        return Prompt.builder()
            .withId(name)
            .withVariations(new ArrayList<>(values))
            .build();
    }
}
