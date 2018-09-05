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

import com.amazon.ask.Skill;
import com.amazon.ask.interaction.SkillApplication;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.types.intent.StandardIntent;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a {@link SkillApplication} {@link JavaFile} for a set of intents.
 */
public class SkillFileGenerator {

    /**
     * @param invocationNames localized invocation names
     * @param intents intent definition and localized data sources
     * @param slots slot type definitions and localized data sources
     * @param namespace java namespace of generated skill
     * @param skillName name of skill - corresponds to generated java class name
     * @return generated java file representing the skill
     */
    public JavaFile generate(Map<Locale, String> invocationNames,
                             Map<IntentDefinition, Map<Locale, IntentData>> intents,
                             Map<SlotTypeDefinition, Map<Locale, SlotTypeData>> slots,
                             String namespace,
                             String skillName) {
        CodeBlock.Builder defineSkillBlock = CodeBlock.builder()
            .add("return $T.builder()\n", SkillModel.class)
            .indent()
            .indent();
        for (Map.Entry<Locale, String> invocationName: invocationNames.entrySet()) {
            defineSkillBlock.add(".withInvocationName($T.forLanguageTag($S), $S)\n", Locale.class, invocationName.getKey().toLanguageTag(), invocationName.getValue());
        }
        if (!intents.isEmpty()) {
            defineSkillBlock.add(".addModel($T.builder()\n", Model.class).indent();
            for (IntentDefinition intentDefinition : intents.keySet()) {
                if (intentDefinition.isCustom()) {
                    defineSkillBlock.add(".intent($T.class)\n", ClassName.get(namespace + ".intents", intentDefinition.getName()));
                } else {
                    TypeName intentClass = TypeNames.get(intentDefinition.getName());

                    if (intents.get(intentDefinition).values().stream().anyMatch(i -> !i.isEmpty())) {
                        defineSkillBlock.add(".intent($T.class, $T.resource()\n" +
                            "    .withResourceClass(getClass())\n" +
                            "    .withName($S)\n" +
                            "    .build())\n", intentClass, IntentData.class, "intents/data/" + builtInIntentName(intentClass));
                    } else {
                        try {
                            defineSkillBlock.add(".intent($T.class)\n", intentClass);
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            for (SlotTypeDefinition slotType : slots.keySet()) {
                if (!slotType.isCustom()) {
                    if (slots.get(slotType).values().stream().anyMatch(s -> !s.isEmpty())) {
                        defineSkillBlock.add(".slotType($T.class, $T.resource()\n" +
                            "    .withResourceClass(getClass())\n" +
                            "    .withName($S)\n" +
                            "    .build())", TypeNames.get(slotType.getName()), SlotTypeData.class, "slots/data/" + slotType.getName());
                    }
                }
            }

            defineSkillBlock.add(".build())\n").unindent();
        }
        defineSkillBlock
            .addStatement(".build()")
            .unindent()
            .unindent();

        TypeSpec skillType = TypeSpec
            .classBuilder(ClassName.get(namespace, skillName))
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(SkillApplication.class)
            .addMethod(MethodSpec
                .methodBuilder("getSkillModel")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(SkillModel.class)
                .addCode(defineSkillBlock.build())
                .build())
            .addMethod(MethodSpec.methodBuilder("getSkill")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(Skill.class)
                .addCode(CodeBlock.builder()
                    .addStatement("throw new $T($S)", RuntimeException.class, "TODO")
                    .build())
                .build())
            .build();

        return JavaFile.builder(namespace, skillType).build();
    }

    private static String builtInIntentName(TypeName typeName) {
        try {
            return Class.forName(typeName.toString()).getAnnotation(Intent.class).value();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean isBuiltIn(TypeName typeName) {
        try {
            return StandardIntent.class.isAssignableFrom(Class.forName(typeName.toString()));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
