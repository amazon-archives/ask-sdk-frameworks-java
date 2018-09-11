/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.annotation.data;

import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.annotation.plugin.AutoIntentData;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.renderer.RenderContext;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.source.Codec;
import com.amazon.ask.interaction.data.source.JsonCodec;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Associates a resource file containing {@link IntentData} with a type annotated with {@link Intent}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(IntentResource.Container.class)
@AutoIntentData(IntentResource.Plugin.class)
public @interface IntentResource {
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
     * @return class of {@link Codec} which can read {@link IntentData} from a file.
     */
    Class<? extends Codec<IntentData>> codec() default DefaultCodec.class;

    class DefaultCodec extends JsonCodec<com.amazon.ask.interaction.data.model.IntentData> {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        public DefaultCodec() {
            super(MAPPER.readerFor(com.amazon.ask.interaction.data.model.IntentData.class));
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
