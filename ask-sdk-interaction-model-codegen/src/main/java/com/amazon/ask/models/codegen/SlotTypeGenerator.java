package com.amazon.ask.models.codegen;

import com.amazon.ask.models.annotation.data.SlotTypeResource;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.amazon.ask.models.types.slot.BaseSlotValue;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Generates a {@link JavaFile} for a {@link SlotTypeDefinition}
 */
public class SlotTypeGenerator {

    /**
     * @param slotType
     * @param namespace java namespace of slot type
     * @return generated java file for this slot type
     */
    public JavaFile generate(SlotTypeDefinition slotType, String namespace) {
        return JavaFile
            .builder(namespace + ".slots", TypeSpec.classBuilder(ClassName.get(namespace + ".slots", slotType.getName()))
                .addModifiers(Modifier.PUBLIC)
                .superclass(BaseSlotValue.class)
                .addAnnotation(AnnotationSpec.builder(SlotType.class)
                    .addMember("value", "$S", slotType.getName())
                    .build())
                .addAnnotation(AnnotationSpec.builder(SlotTypeResource.class)
                    .addMember("value", "$S", "data/" + slotType.getName())
                    .build())
                .build())
            .build();
    }
}
