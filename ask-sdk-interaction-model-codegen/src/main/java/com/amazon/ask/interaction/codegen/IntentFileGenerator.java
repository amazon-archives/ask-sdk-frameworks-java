package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Generates a {@link JavaFile} for a {@link IntentDefinition}
 */
public class IntentFileGenerator {

    /**
     * @param intentDefinition
     * @param namespace java namespace of intent
     * @return generated java file for this intent
     */
    JavaFile generate(IntentDefinition intentDefinition, String namespace) {
        TypeSpec.Builder typeBuilder = TypeSpec
            .classBuilder(intentDefinition.getName())
            .addAnnotation(AnnotationSpec.builder(Intent.class)
                .addMember("value", "$S", intentDefinition.getName())
                .build())
            .addAnnotation(AnnotationSpec.builder(IntentResource.class)
                .addMember("value", "$S", "data/" + intentDefinition.getName())
                .build())
            .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder equalsBuilder = MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(Boolean.TYPE)
            .addParameter(Object.class, "o")
            .addStatement("if (this == o) return true")
            .addStatement("if (o == null || getClass() != o.getClass()) return false");

        MethodSpec.Builder hashCodeBuilder = MethodSpec.methodBuilder("hashCode")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(Integer.TYPE);

        Iterator<Map.Entry<String, SlotTypeDefinition>> slotIterator = intentDefinition.getSlots().entrySet().iterator();
        if (!slotIterator.hasNext()) {
            equalsBuilder.addStatement("return true");
            hashCodeBuilder.addStatement("return 0");
        } else {
            equalsBuilder
                .addStatement("$L that = ($L) o", intentDefinition.getName(), intentDefinition.getName())
                .addCode("return ");
            hashCodeBuilder.addCode("return $T.hash(", Objects.class);
        }

        while (slotIterator.hasNext()) {
            Map.Entry<String, SlotTypeDefinition> entry = slotIterator.next();
            String slotName = entry.getKey();
            SlotTypeDefinition slotTypeDefinition = entry.getValue();
            TypeName type;
            if (slotTypeDefinition.isCustom()) {
                type = ClassName.get(namespace + ".slots", slotTypeDefinition.getName());
            } else {
                type = TypeNames.get(slotTypeDefinition.getName());
            }
            if (type == null) {
                throw new IllegalStateException("Unknown type: " + slotTypeDefinition.getName());
            }

            typeBuilder.addField(FieldSpec
                .builder(type, slotName, Modifier.PRIVATE)
                .addAnnotation(SlotProperty.class)
                .build());

            String beanName = slotName.substring(0, 1).toUpperCase() + slotName.substring(1);

            typeBuilder.addMethod(MethodSpec.methodBuilder("get" + beanName)
                .returns(type)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$L", slotName)
                .build());

            typeBuilder.addMethod(MethodSpec.methodBuilder("set" + beanName)
                .returns(Void.TYPE)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, slotName)
                .addStatement("this.$L = $L", slotName, slotName)
                .build());

            if (slotIterator.hasNext()) {
                equalsBuilder.addCode("$T.equals($L, that.$L) && ", Objects.class, slotName, slotName);
                hashCodeBuilder.addCode("$L, ", slotName);
            } else {
                equalsBuilder.addStatement("$T.equals($L, that.$L)", Objects.class, slotName, slotName);
                hashCodeBuilder.addStatement("$L)", slotName);
            }
        }

        typeBuilder.addMethod(equalsBuilder.build());
        typeBuilder.addMethod(hashCodeBuilder.build());

        return JavaFile.builder(namespace + ".intents", typeBuilder.build()).build();
    }
}
