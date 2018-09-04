package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.*;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.IntentSlotData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;

import java.util.*;
import java.util.stream.Collectors;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders interaction model components for Intents
 */
public class IntentRenderer {
    private final IntentSlotRenderer intentSlotRenderer;

    public IntentRenderer(IntentSlotRenderer intentSlotRenderer) {
        this.intentSlotRenderer = assertNotNull(intentSlotRenderer, "intentSlotRenderer");
    }

    public IntentRenderer() {
        this(new IntentSlotRenderer());
    }

    /**
     * Decorate an intent definition add localized data to render its {@link Intent} structure.
     *
     * @param intentDefinition the intent's definition
     * @param intentData the intent's data
     * @return rendered intent definition
     */
    public Intent renderIntent(IntentDefinition intentDefinition, IntentData intentData) {
        List<Slot> slots = intentDefinition.getSlots().entrySet().stream()
            .map(s -> intentSlotRenderer.renderSlot(s.getKey(), s.getValue(), intentData))
            .collect(Collectors.toList());

        return Intent.builder()
            .withName(intentDefinition.getName())
            .withSlots(slots.isEmpty() ? null : slots)
            .withSamples(intentData.getSamples().isEmpty() ? null : new ArrayList<>(intentData.getSamples()))
            .build();
    }

    /**
     * Render an intent's {@link DialogIntent}
     *
     * - Intent confirmation prompts.
     * - Each slot's confirmations and elicitation prompts.
     *
     * A Dialog is not mandatory because an intent may have zero dialog/prompt specifications. An unit optional is
     * returned in this case.
     *
     * @param intentDefinition the intent's definition
     * @param intentData the intent's data
     * @return rendered intent dialog definition
     */
    public Optional<DialogIntent> renderDialogIntent(IntentDefinition intentDefinition, IntentData intentData) {
        if (hasDialogInformation(intentData)) {
            DialogIntent intent = DialogIntent.builder()
                .withName(intentDefinition.getName())
                .withSlots(renderDialogSlots(intentDefinition, intentData).orElse(null))
                .withConfirmationRequired(intentData.getConfirmationRequired())
                .withPrompts(resolveConfirmationPrompt(intentDefinition, intentData))
                .build();

            return Optional.of(intent);
        }
        return Optional.empty();
    }

    // We only want to render dialog information if it is required
    // Look for confirmation/elicitation flags, prompts, etc.
    private static boolean hasDialogInformation(IntentData intentData) {
        return !intentData.getConfirmations().isEmpty()
            || (intentData.getPrompts() != null &&  intentData.getPrompts().getConfirmation() != null)
            || intentData.getConfirmationRequired() != null
            || intentData.getSlots().values().stream().anyMatch(IntentRenderer::hasDialogInformation);
    }

    private static boolean hasDialogInformation(IntentSlotData slot) {
        return slot.getConfirmationRequired() != null
            || slot.getElicitationRequired() != null
            || !slot.getConfirmations().isEmpty()
            || !slot.getElicitations().isEmpty()
            || (slot.getPrompts() != null && (slot.getPrompts().getElicitation() != null || slot.getPrompts().getConfirmation() != null));
    }

    protected Optional<List<DialogSlot>> renderDialogSlots(IntentDefinition intentDefinition, IntentData intentData) {
        List<DialogSlot> slots = new ArrayList<>();
        for (Map.Entry<String, SlotTypeDefinition> slot: intentDefinition.getSlots().entrySet()) {
            IntentSlotData slotData = intentData.getSlots().get(slot.getKey());
            if (slotData != null && hasDialogInformation(slotData)) {
                slots.add(intentSlotRenderer.renderDialogSlot(intentDefinition, slot.getKey(), slot.getValue(), slotData));
            }
        }
        return slots.isEmpty() ? Optional.empty() : Optional.of(slots);
    }

    /**
     * Render an intent's (and its slot's) dialog confirmation and elicitation prompts.
     *
     * @param intentDefinition the intent's definition
     * @param intentData the intent's data
     * @return rendered list of prompts for this intent
     */
    public List<Prompt> renderPrompts(IntentDefinition intentDefinition, IntentData intentData) {
        List<Prompt> prompts = new ArrayList<>();
        Set<PromptVariation> confirmations = intentData.getConfirmations();

        DialogIntentPrompt confirmPrompt = resolveConfirmationPrompt(intentDefinition, intentData);
        if (confirmPrompt != null && !confirmations.isEmpty()) {
            prompts.add(Prompt.builder()
                .withId(confirmPrompt.getConfirmation())
                .withVariations(new ArrayList<>(confirmations))
                .build());
        }

        for (Map.Entry<String, SlotTypeDefinition> slot: intentDefinition.getSlots().entrySet()) {
            IntentSlotData slotData = intentData.getSlots().get(slot.getKey());
            if (slotData != null) {
                prompts.addAll(intentSlotRenderer.renderSlotPrompts(intentDefinition, slot.getKey(), slotData));
            }
        }

        return prompts;
    }

    protected DialogIntentPrompt resolveConfirmationPrompt(IntentDefinition intentDefinition, IntentData intentData) {
        String promptId = null;
        if (intentData.getPrompts() != null) {
            promptId = intentData.getPrompts().getConfirmation();
        } else if (intentData.getConfirmations() != null && !intentData.getConfirmations().isEmpty()) {
            promptId = "Confirm.Intent-" + intentDefinition.getName();
        }
        return promptId == null ? null : DialogIntentPrompt.builder().withConfirmation(promptId).build();
    }
}
