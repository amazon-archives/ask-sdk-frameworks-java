/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.DialogIntentPrompt;
import com.amazon.ask.interaction.model.PromptVariation;
import com.amazon.ask.interaction.Locales;
import com.amazon.ask.interaction.annotation.data.IntentSkillResource;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.IntentSlotData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class IntentSkillResourceTest {
    private final IntentSkillResource.Plugin underTest = new IntentSkillResource.Plugin();

    @Mock
    private IntentDefinition mockIntentDefinition;

    private RenderContext<IntentDefinition> input;

    IntentSkillResource defaultIntentSkillResource = DefaultModel.class.getAnnotation(IntentSkillResource.class);
    IntentSkillResource emptytIntentSkillResource = EmptyModel.class.getAnnotation(IntentSkillResource.class);

    @Before
    public void prepareInput() {
        Mockito.when(mockIntentDefinition.getIntentType()).thenReturn(TypeFactory.defaultInstance()
            .constructSimpleType(DefaultModel.class, new JavaType[]{}));
        input = RenderContext.intent()
            .withLocale(Locales.en_US)
            .withValue(mockIntentDefinition)
            .build();
    }

    @IntentSkillResource("interaction_model_test")
    public static class DefaultModel {

    }

    @IntentSkillResource("interaction_model_test_empty")
    public static class EmptyModel {

    }

    @Test
    public void testMissing() {
        Mockito.when(mockIntentDefinition.getName()).thenReturn("missing");
        IntentData expected = IntentData.builder().build();
        IntentData actual = underTest.apply(input, defaultIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyIntent() {
        Mockito.when(mockIntentDefinition.getName()).thenReturn("empty_intent");
        IntentData expected = IntentData.builder().build();
        IntentData actual = underTest.apply(input, defaultIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIntentSamples() {
        Mockito.when(mockIntentDefinition.getName()).thenReturn("intent_samples");
        IntentData expected = IntentData.builder()
            .addSamples(Collections.singletonList("sample"))
            .build();
        IntentData actual = underTest.apply(input, defaultIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIntentConfirmations() {
        Mockito.when(mockIntentDefinition.getName()).thenReturn("intent_confirmations");
        IntentData expected = IntentData.builder()
            .withConfirmationRequired(false)
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("Confirm.Intent-intent_confirmations")
                .build())
            .addConfirmation(PromptVariation.builder()
                .withType("PlainText")
                .withValue("confirmation")
                .build())
            .build();
        IntentData actual = underTest.apply(input, defaultIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIntentSlotConfirmations() {
        Mockito.when(mockIntentDefinition.getName()).thenReturn("intent_slot_confirmations");
        IntentData expected = IntentData.builder()
            .addSlot("slot", IntentSlotData.builder()
                .withConfirmationRequired(true)
                .addConfirmation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("confirmation")
                    .build())
                .build())
            .build();
        IntentData actual = underTest.apply(input, defaultIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIntentSlotElicitations() {
        Mockito.when(mockIntentDefinition.getName()).thenReturn("intent_slot_elicitations");
        IntentData expected = IntentData.builder()
            .addSlot("slot", IntentSlotData.builder()
                .withElicitationRequired(true)
                .addElicitation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("elicitation")
                    .build())
                .build())
            .build();
        IntentData actual = underTest.apply(input, defaultIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }


    @Test
    public void testEmptyInteractionModel() {
        Mockito.when(mockIntentDefinition.getIntentType()).thenReturn(TypeFactory.defaultInstance().constructSimpleType(EmptyModel.class, new JavaType[]{}));
        Mockito.when(mockIntentDefinition.getName()).thenReturn("empty_intent");
        IntentData expected = IntentData.builder().build();
        IntentData actual = underTest.apply(input, emptytIntentSkillResource).reduce(IntentData::combine).get();
        assertEquals(expected, actual);
    }
}
