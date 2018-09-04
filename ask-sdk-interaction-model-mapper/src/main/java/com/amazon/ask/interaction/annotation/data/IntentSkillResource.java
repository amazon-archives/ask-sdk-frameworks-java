package com.amazon.ask.interaction.annotation.data;

import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.model.*;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.annotation.plugin.AutoIntentData;
import com.amazon.ask.interaction.renderer.RenderContext;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.IntentSlotData;
import com.amazon.ask.interaction.data.source.Codec;
import com.amazon.ask.interaction.data.source.JsonCodec;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Associates a resource file containing {@link SkillModel} with a type annotated with {@link Intent}.
 *
 * Using the intent's name, its {@link IntentData} is cherry-picked from the skill model file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoIntentData(IntentSkillResource.Plugin.class)
@Repeatable(IntentSkillResource.Container.class)
public @interface IntentSkillResource {
    /**
     * @return resource name containing data.
     */
    String value();

    /**
     * @return suffix of resource file - defaults to .json.
     */
    String suffix() default ".json";

    /**
     * @return class to load resources from, defaults to the annotated class.
     */
    Class<?> resourceClass() default Object.class;

    /**
     * @return class of {@link Codec} to read {@link SkillModel} data
     */
    Class<? extends Codec<SkillModel>> codec() default DefaultCodec.class;

    /**
     * Parse the standard Skill Model JSON Schema, defined by {@link SkillModel}.
     */
    class DefaultCodec extends JsonCodec<SkillModel> {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        public DefaultCodec() {
            super(MAPPER.readerFor(SkillModel.class));
        }
    }

    /**
     * Reads {@link IntentData} from an {@link InteractionModel}.
     *
     * Data for this intent is cherry-picked from the interaction model.
     */
    class Plugin implements AutoIntentData.Plugin<IntentSkillResource> {
        @Override
        public Stream<IntentData> apply(RenderContext<IntentDefinition> context, IntentSkillResource annotation) {
            /*
                Read this intent's data from the skill model
             */
            Codec<IntentData> transformingCodec = stream -> read(context, stream, Utils.instantiate(annotation.codec()));

            IntentData.Resource intentData = IntentData.resource()
                .withName(annotation.value())
                .withSuffix(annotation.suffix())
                .withCodec(transformingCodec)
                .withResourceClass(annotation.resourceClass() == Object.class ? context.getValue().getIntentType().getRawClass() : annotation.resourceClass())
                .build();

            return Stream.of(intentData).map(s -> s.apply(context));
        }

        protected IntentData read(RenderContext<IntentDefinition> input, InputStream resource, Codec<SkillModel> codec) throws IOException {
            InteractionModel model = codec.read(resource).getInteractionModel();

            IntentData.Builder builder = IntentData.builder();

            extractSamples(input, builder, model);
            extractDialogs(input, builder, model);

            return builder.build();
        }

        protected void extractSamples(RenderContext<IntentDefinition> input, IntentData.Builder intentMetadata, InteractionModel model) {
            model.getLanguageModel().getIntents().stream()
                .filter(i -> i.getName().equals(input.getValue().getName()))
                .findFirst()
                .ifPresent(intent -> {
                    intentMetadata.withSamples(intent.getSamples());
                    if (intent.getSlots() != null) {
                        for (Slot slot : intent.getSlots()) {
                            intentMetadata.addSlot(
                                slot.getName(),
                                IntentSlotData.builder()
                                    .withSamples(slot.getSamples())
                                    .build());
                        }
                    }
                });
        }

        protected void extractDialogs(RenderContext<IntentDefinition> input, IntentData.Builder intentMetadata, InteractionModel model) {
            Map<String, List<PromptVariation>> prompts = indexPrompts(model.getPrompts());
            Dialog dialog = model.getDialog();

            if (dialog != null) {
                dialog.getIntents().stream()
                    .filter(i -> i.getName().equals(input.getValue().getName()))
                    .findFirst()
                    .ifPresent(dialogIntent -> {
                        DialogIntentPrompt dialogIntentPrompts = dialogIntent.getPrompts();
                        intentMetadata
                            .withConfirmationRequired(dialogIntent.getConfirmationRequired())
                            .withPrompts(dialogIntentPrompts);

                        if (dialogIntentPrompts != null && dialogIntentPrompts.getConfirmation() != null) {
                            Optional
                                .ofNullable(prompts.get(dialogIntentPrompts.getConfirmation()))
                                .ifPresent(intentMetadata::addConfirmations);
                        }

                        if (dialogIntent.getSlots() != null) {
                            for (DialogSlot dialogSlot: dialogIntent.getSlots()) {
                                intentMetadata.addSlot(dialogSlot.getName(), extractSlotDialogs(prompts, dialogSlot));
                            }
                        }
                    });
            }
        }

        protected Map<String, List<PromptVariation>> indexPrompts(List<Prompt> prompts) {
            if (prompts == null) {
                return Collections.emptyMap();
            } else {
                return prompts.stream().collect(Collectors.toMap(Prompt::getId, Prompt::getVariations));
            }
        }

        protected IntentSlotData extractSlotDialogs(Map<String, List<PromptVariation>> prompts, DialogSlot dialogSlot) {
            IntentSlotData.Builder intentSlot = IntentSlotData.builder()
                .withConfirmationRequired(dialogSlot.getConfirmationRequired())
                .withElicitationRequired(dialogSlot.getElicitationRequired());

            if (dialogSlot.getPrompts() != null) {
                Optional
                    .ofNullable(prompts.get(dialogSlot.getPrompts().getConfirmation()))
                    .map(HashSet::new)
                    .ifPresent(intentSlot::addConfirmations);

                Optional
                    .ofNullable(prompts.get(dialogSlot.getPrompts().getElicitation()))
                    .map(HashSet::new)
                    .ifPresent(intentSlot::addElicitations);
            }

            return intentSlot.build();
        }
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    @AutoIntentData(IntentSkillResource.Container.Plugin.class)
    @interface Container {
        IntentSkillResource[] value();

        class Plugin implements AutoIntentData.Plugin<Container> {
            private static final IntentSkillResource.Plugin SINGLE = new IntentSkillResource.Plugin();

            @Override
            public Stream<IntentData> apply(RenderContext<IntentDefinition> input, Container container) {
                return Arrays.stream(container.value()).flatMap(a -> SINGLE.apply(input, a));
            }
        }
    }
}
