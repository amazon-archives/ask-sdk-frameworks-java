package com.amazon.ask.interaction.annotation.data;

import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.annotation.plugin.AutoSlotTypeData;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.data.source.Codec;
import com.amazon.ask.interaction.data.source.JsonCodec;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.renderer.RenderContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Associates a resource file containing {@link SlotTypeData} with a type annotated with {@link SlotType}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoSlotTypeData(SlotTypeResource.Plugin.class)
@Repeatable(SlotTypeResource.Container.class)
public @interface SlotTypeResource {
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
    Class<?> scope() default Object.class;

    /**
     * @return class of {@link Codec} to read {@link SlotTypeData} data
     */
    Class<? extends Codec<SlotTypeData>> codec() default DefaultCodec.class;

    class DefaultCodec extends JsonCodec<SlotTypeData> {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        public DefaultCodec() {
            super(MAPPER.readerFor(SlotTypeData.class));
        }
    }

    class Plugin implements AutoSlotTypeData.Plugin<SlotTypeResource> {
        @Override
        public Stream<SlotTypeData> apply(RenderContext<SlotTypeDefinition> entity, SlotTypeResource annotation) {
            SlotTypeData.Resource.Builder slotTypeData = SlotTypeData.resource()
                .withName(annotation.value())
                .withSuffix(annotation.suffix())
                .withResourceClass(annotation.scope() == Object.class ? entity.getValue().getSlotTypeClass() : annotation.scope());

            if (annotation.codec() != SlotTypeResource.DefaultCodec.class) {
                slotTypeData.withCodec(Utils.instantiate(annotation.codec()));
            }

            return Stream.of(slotTypeData.build().apply(entity));
        }
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    @AutoSlotTypeData(SlotTypeResource.Container.Plugin.class)
    @interface Container {
        SlotTypeResource[] value();

        class Plugin implements AutoSlotTypeData.Plugin<Container> {
            private static final SlotTypeResource.Plugin SINGLE = new SlotTypeResource.Plugin();

            @Override
            public Stream<SlotTypeData> apply(RenderContext<SlotTypeDefinition> input, Container annotation) {
                return Arrays.stream(annotation.value()).flatMap(a -> SINGLE.apply(input, a));
            }
        }
    }
}
