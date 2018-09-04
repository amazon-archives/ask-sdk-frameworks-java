package com.amazon.ask.models.annotation.plugin;

import com.amazon.ask.models.Utils;
import com.amazon.ask.models.annotation.type.Intent;
import com.amazon.ask.models.data.IntentDataResolver;
import com.amazon.ask.models.renderer.RenderContext;
import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.definition.IntentDefinition;

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
