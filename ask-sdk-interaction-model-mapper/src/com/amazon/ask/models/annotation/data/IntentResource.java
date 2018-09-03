package com.amazon.ask.models.annotation.data;

import com.amazon.ask.models.Utils;
import com.amazon.ask.models.annotation.plugin.AutoIntentData;
import com.amazon.ask.models.renderer.RenderContext;
import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.data.source.Codec;
import com.amazon.ask.models.data.source.JsonCodec;
import com.amazon.ask.models.definition.IntentDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@Repeatable(IntentResource.Container.class)
@AutoIntentData(IntentResource.Plugin.class)
public @interface IntentResource {
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

    /**
     * Class of {@link Codec} which can read {@link IntentData} from a file.
     */
    Class<? extends Codec<IntentData>> codec() default DefaultCodec.class;

    class DefaultCodec extends JsonCodec<com.amazon.ask.models.data.model.IntentData> {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        public DefaultCodec() {
            super(MAPPER.readerFor(com.amazon.ask.models.data.model.IntentData.class));
        }
    }

    class Plugin implements AutoIntentData.Plugin<IntentResource> {
        @Override
        public Stream<IntentData> apply(RenderContext<IntentDefinition> context, IntentResource annotation) {
            IntentData.Resource.Builder intentData = IntentData.resource()
                .withName(annotation.value())
                .withSuffix(annotation.suffix())
                .withResourceClass(annotation.resourceClass() == Object.class ? context.getValue().getIntentType().getRawClass() : annotation.resourceClass());

            if (annotation.codec() != IntentResource.DefaultCodec.class) {
                intentData.withCodec(Utils.instantiate(annotation.codec()));
            }

            return Stream.of(intentData.build()).map(s -> s.apply(context));
        }
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    @AutoIntentData(IntentResource.Container.Plugin.class)
    @interface Container {
        IntentResource[] value();

        class Plugin implements AutoIntentData.Plugin<Container> {
            private static final IntentResource.Plugin SINGLE = new IntentResource.Plugin();

            @Override
            public Stream<IntentData> apply(RenderContext<IntentDefinition> input, Container annotation) {
                return Arrays.stream(annotation.value()).flatMap(a -> SINGLE.apply(input, a));
            }
        }
    }
}
