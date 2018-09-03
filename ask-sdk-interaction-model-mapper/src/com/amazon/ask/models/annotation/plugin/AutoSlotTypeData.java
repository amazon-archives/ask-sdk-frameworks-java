package com.amazon.ask.models.annotation.plugin;

import com.amazon.ask.models.Utils;
import com.amazon.ask.models.data.SlotTypeDataResolver;
import com.amazon.ask.models.renderer.RenderContext;
import com.amazon.ask.models.data.model.SlotTypeData;
import com.amazon.ask.models.definition.SlotTypeDefinition;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Attach to an annotation that defines data for a class annotated add {@link com.amazon.ask.models.annotation.type.SlotType}
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoSlotTypeData {
    Class<? extends AutoSlotTypeData.Plugin<? extends Annotation>> value();

    interface Plugin<A extends Annotation>
        extends BiFunction<RenderContext<SlotTypeDefinition>, A, Stream<SlotTypeData>> {}

    class Scanner implements SlotTypeDataResolver {
        @Override
        @SuppressWarnings("unchecked")
        public Stream<SlotTypeData> apply(RenderContext<SlotTypeDefinition> input) {
            return Utils.getSuperclasses(input.getValue().getSlotTypeClass())
                .map(Class::getAnnotations)
                .flatMap(Arrays::stream)
                .filter(a -> a.annotationType().getAnnotation(AutoSlotTypeData.class) != null)
                .flatMap(annotation -> {
                    AutoSlotTypeData meta = annotation.annotationType().getAnnotation(AutoSlotTypeData.class);
                    if (meta != null) {
                        AutoSlotTypeData.Plugin<Annotation> plugin = (AutoSlotTypeData.Plugin<Annotation>) Utils.instantiate(meta.value());
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
