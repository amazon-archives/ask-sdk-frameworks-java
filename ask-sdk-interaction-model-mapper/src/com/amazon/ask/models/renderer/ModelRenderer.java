package com.amazon.ask.models.renderer;

import com.amazon.ask.interaction.model.*;
import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.data.model.SlotTypeData;
import com.amazon.ask.models.data.model.SubLanguageModel;
import com.amazon.ask.models.data.model.SubModel;
import com.amazon.ask.models.definition.IntentDefinition;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.definition.SlotTypeDefinition;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders the {@link SubModel} for a {@link Model}
 */
public class ModelRenderer {
    private final IntentRenderer intentRenderer;
    private final SlotTypeRenderer slotTypeRenderer;

    public ModelRenderer(IntentRenderer intentRenderer, SlotTypeRenderer slotTypeRenderer) {
        this.intentRenderer = assertNotNull(intentRenderer, "intentRenderer");
        this.slotTypeRenderer = assertNotNull(slotTypeRenderer, "slotTypeRenderer");
    }

    public ModelRenderer() {
        this(new IntentRenderer(), new SlotTypeRenderer());
    }

    /**
     * Renders the localized {@link SubModel} for a {@link Model}
     *
     * @param model
     * @param locale
     * @return rendered model
     * @throws IOException if fetching and compiling metadata fails
     */
    public SubModel render(Model model, Locale locale) {
        List<Intent> intents = new ArrayList<>();
        List<DialogIntent> dialogIntents = new ArrayList<>();

        Map<SlotTypeDefinition, com.amazon.ask.interaction.model.SlotType> types = new LinkedHashMap<>();
        Map<String, Prompt> prompts = new LinkedHashMap<>();

        for (IntentDefinition intentDefinition: model.getIntentDefinitions().values()) {
            RenderContext<IntentDefinition> renderContext = RenderContext.intent()
                .withValue(intentDefinition)
                .withLocale(locale)
                .build();

            // apply all the resolvers to the intent
            Stream<IntentData> resolvedData = model.getIntentDataResolvers().stream()
                .flatMap(resolver -> resolver.apply(renderContext));

            // then gather all the data mapped to this intent
            Stream<IntentData> associatedData = Optional.ofNullable(model.getIntentDataSources().get(intentDefinition))
                .map(Stream::of).orElse(Stream.empty())
                .flatMap(Set::stream)
                .map(resolver -> resolver.apply(renderContext));

            // combine them all into a single instance, or yield an empty instance
            IntentData intentData = Stream.concat(resolvedData, associatedData)
                .reduce(IntentData::combine)
                .orElse(IntentData.empty());

            intents.add(intentRenderer.renderIntent(intentDefinition, intentData));
            intentRenderer
                .renderDialogIntent(intentDefinition, intentData)
                .ifPresent(dialogIntents::add);

            renderPrompts(intentDefinition, intentData, prompts);
        }

        for (SlotTypeDefinition slotType : model.getSlotTypes().values()) {
            RenderContext<SlotTypeDefinition> renderContext = RenderContext.slotType()
                .withLocale(locale)
                .withValue(slotType)
                .build();

            Stream<SlotTypeData> resolvedData = model.getSlotTypeDataResolvers().stream()
                .flatMap(resolver -> resolver.apply(renderContext));

            Stream<SlotTypeData> mappedData = Optional.ofNullable(model.getSlotTypeDataSources().get(slotType))
                .map(Stream::of).orElse(Stream.empty())
                .flatMap(Set::stream)
                .map(mapper -> mapper.apply(renderContext));

            SlotTypeData slotTypeData = Stream.concat(resolvedData, mappedData)
                .reduce(SlotTypeData::combine)
                .orElse(SlotTypeData.empty());

            if (!slotTypeData.isEmpty()) {
                types.put(slotType, slotTypeRenderer.renderSlotType(slotType, slotTypeData));
            }
        }

        SubLanguageModel languageModel = SubLanguageModel.builder()
            .withIntents(intents)
            .withTypes(new ArrayList<>(types.values()))
            .build();

        return SubModel.builder()
            .withLanguageModel(languageModel)
            .withDialog(dialogIntents.isEmpty() ? null : Dialog.builder().withIntents(dialogIntents).build())
            .withPrompts(prompts.isEmpty() ? null : new ArrayList<>(prompts.values()))
            .build();
    }

    private void renderPrompts(IntentDefinition intentDefinition, IntentData intentData, Map<String, Prompt> prompts) {
        for (Prompt prompt : intentRenderer.renderPrompts(intentDefinition, intentData)) {
            Prompt updatedPrompt = prompts.get(prompt.getId());
            if (updatedPrompt == null) {
                updatedPrompt = prompt;
            } else {
                List<PromptVariation> variations = prompt.getVariations();
                variations.addAll(updatedPrompt.getVariations());
                updatedPrompt = Prompt.builder()
                    .withId(prompt.getId())
                    .withVariations(variations)
                    .build();
            }

            prompts.put(updatedPrompt.getId(), updatedPrompt);
        }
    }
}
