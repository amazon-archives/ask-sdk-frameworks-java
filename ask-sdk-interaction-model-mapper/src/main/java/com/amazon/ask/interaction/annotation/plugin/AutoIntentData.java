/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.annotation.plugin;

import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.data.IntentDataResolver;
import com.amazon.ask.interaction.renderer.RenderContext;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.definition.IntentDefinition;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Attach to an annotation that defines data for a class annotated add {@link Intent}
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIntentData {
    Class<? extends AutoIntentData.Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<RenderContext<IntentDefinition>, A, Stream<IntentData>> {}

    class Scanner implements IntentDataResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Stream<IntentData> apply(RenderContext<IntentDefinition> input) {
            return Utils.getSuperclasses(input.getValue().getIntentType().getRawClass())
                .map(Class::getAnnotations)
                .flatMap(Arrays::stream)
                .filter(a -> a.annotationType().getAnnotation(AutoIntentData.class) != null)
                .flatMap(annotation -> {
                    AutoIntentData meta = annotation.annotationType().getAnnotation(AutoIntentData.class);
                    if (meta != null) {
                        AutoIntentData.Plugin<Annotation> plugin = (AutoIntentData.Plugin<Annotation>) Utils.instantiate(meta.value());
                        return plugin.apply(input, annotation);
                    } else {
                        return Stream.empty();
                    }
                });
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object other) {
            // de-dupe instances of this exact class
            // TODO: gross?
            return other != null && other.getClass() == getClass();
        }
    }
}
