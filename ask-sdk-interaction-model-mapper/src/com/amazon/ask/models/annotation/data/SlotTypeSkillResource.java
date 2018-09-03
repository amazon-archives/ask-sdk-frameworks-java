package com.amazon.ask.models.annotation.data;

import com.amazon.ask.interaction.model.InteractionModel;
import com.amazon.ask.interaction.model.SkillModel;
import com.amazon.ask.interaction.model.SlotTypeValue;
import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.models.Utils;
import com.amazon.ask.models.annotation.plugin.AutoSlotTypeData;
import com.amazon.ask.models.renderer.RenderContext;
import com.amazon.ask.models.data.model.SlotTypeData;
import com.amazon.ask.models.data.source.Codec;
import com.amazon.ask.models.data.source.JsonCodec;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoSlotTypeData(SlotTypeSkillResource.Plugin.class)
@Repeatable(SlotTypeSkillResource.Container.class)
public @interface SlotTypeSkillResource {
    /**
     * Resource name containing data.
     */
    String value();

    /**
     * Suffix of resource file - defaults to .json.
     */
    String suffix() default ".json";

    /**
     * Class to load resources from, defaults to the annotated class.
     */
    Class<?> resourceClass() default Object.class;

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
     * Reads {@link SlotTypeData} from an {@link InteractionModel}
     *
     * Information for this slot type is cherry-picked from the interaction model.
     */
    class Plugin implements AutoSlotTypeData.Plugin<SlotTypeSkillResource> {
        @Override
        public Stream<SlotTypeData> apply(RenderContext<SlotTypeDefinition> context, SlotTypeSkillResource annotation) {
            /*
                Read this intent's data from the skill model
             */
            Codec<SlotTypeData> transformingCodec = stream -> read(context, stream, Utils.instantiate(annotation.codec()));

            SlotTypeData.Resource.Builder intentData = SlotTypeData.resource()
                .withName(annotation.value())
                .withSuffix(annotation.suffix())
                .withCodec(transformingCodec)
                .withResourceClass(annotation.resourceClass() == Object.class ? context.getValue().getSlotTypeClass() : annotation.resourceClass());

            return Stream.of(intentData.build()).map(s -> s.apply(context));
        }

        protected SlotTypeData read(RenderContext<SlotTypeDefinition> input, InputStream resource, Codec<SkillModel> codec) throws IOException {
            InteractionModel model = codec.read(resource).getInteractionModel();
            SlotTypeData.Builder slotMetadataBuilder = SlotTypeData.builder();

            if (model.getLanguageModel().getTypes() != null) {
                model
                    .getLanguageModel().getTypes().stream()
                    .filter(slotType -> slotType.getName().equals(input.getValue().getName()))
                    .findFirst()
                    .ifPresent(slotType -> {
                        for (SlotTypeValue slotTypeValue: slotType.getValues()) {
                            slotMetadataBuilder.addValue(
                                slotTypeValue.getId(),
                                SlotValue.builder()
                                    .withValue(slotTypeValue.getName().getValue())
                                    .withSynonyms(slotTypeValue.getName().getSynonyms())
                                    .build());

                        }
                    });
            }

            return slotMetadataBuilder.build();
        }
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    @AutoSlotTypeData(Container.Plugin.class)
    @interface Container {
        SlotTypeSkillResource[] value();

        class Plugin implements AutoSlotTypeData.Plugin<Container> {
            private static final SlotTypeSkillResource.Plugin SINGLE = new SlotTypeSkillResource.Plugin();

            @Override
            public Stream<SlotTypeData> apply(RenderContext<SlotTypeDefinition> input, Container container) {
                return Arrays.stream(container.value()).flatMap(a -> SINGLE.apply(input, a));
            }
        }
    }
}
