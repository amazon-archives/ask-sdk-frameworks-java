/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.annotation.data.SlotTypeResource;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.types.slot.BaseSlotValue;
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
