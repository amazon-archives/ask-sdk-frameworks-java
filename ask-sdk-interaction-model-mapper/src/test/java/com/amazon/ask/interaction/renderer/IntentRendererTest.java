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

import com.amazon.ask.interaction.model.*;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.IntentSlotData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.types.slot.AmazonLiteral;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class IntentRendererTest {
    private final SlotTypeDefinition literalType = SlotTypeDefinition.builder()
        .withCustom(false)
        .withSlotTypeClass(AmazonLiteral.class)
        .withName("slot_type")
        .build();

    private final IntentDefinition intent = IntentDefinition.builder()
        .withName("TestIntent")
        .withCustom(false)
        .withIntentType(Mockito.mock(JavaType.class))
        .withSlots(Collections.singletonMap("slot_name", SlotTypeDefinition.builder()
            .withName("slot_type")
            .withCustom(false)
            .withSlotTypeClass(AmazonLiteral.class)
            .build()))
        .build();

    private final IntentRenderer underTest = new IntentRenderer();



    @Test
    public void testRenderIntent_NoSlots() {
        IntentDefinition intent = IntentDefinition.builder()
            .withName("TestIntent")
            .withCustom(false)
            .withIntentType(Mockito.mock(JavaType.class))
            .build();

        IntentData intentData = IntentData.builder().build();

        Intent expected = Intent.builder()
            .withName("TestIntent")
            .build();
        Intent actual = underTest.renderIntent(intent, intentData);
        assertEquals(expected, actual);
    }

    @Test
    public void testRenderIntent_Slots() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder().build())
            .build();

        Intent expected = Intent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(Slot.builder()
                .withName("slot_name")
                .withType("slot_type")
                .build()))
            .build();

        Intent actual = underTest.renderIntent(intent, intentData);

        assertEquals(expected, actual);
    }

    @Test
    public void testRenderIntent_Samples() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder().build())
            .addSample("test")
            .build();

        Intent expected = Intent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(Slot.builder()
                .withName("slot_name")
                .withType("slot_type")
                .build()))
            .withSamples(Collections.singletonList("test"))
            .build();

        Intent actual = underTest.renderIntent(intent, intentData);

        assertEquals(expected, actual);
    }

    @Test
    public void testRenderDialogIntent_NoDialogInformation() {
        assertEquals(Optional.empty(), underTest.renderDialogIntent(intent, IntentData.builder().build()));
        assertEquals(Optional.empty(), underTest.renderDialogIntent(
            intent, IntentData.builder().addSlot("slot_name", IntentSlotData.builder().build()).build()));
    }

    @Test
    public void testRenderDialogIntent_ExplicitIntentConfirmPrompt() {
        IntentData intentData = IntentData.builder()
            .addConfirmation(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build())
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("Test")
                .build())
            .build();

        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("Test")
                .build())
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Test")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_IntentConfirmation() {
        IntentData intentData = IntentData.builder()
            .addConfirmation(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("Confirm.Intent-TestIntent")
                .build())
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Confirm.Intent-TestIntent")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_IntentConfirmationRequired() {
        IntentData intentData = IntentData.builder()
            .withConfirmationRequired(true)
            .addConfirmation(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withConfirmationRequired(true)
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("Confirm.Intent-TestIntent")
                .build())
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Confirm.Intent-TestIntent")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_ExplicitSlotConfirmPromptId() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder()
                .addConfirmation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("test_prompt")
                    .build())
                .withPrompts(DialogSlotPrompt.builder()
                    .withConfirmation("Test")
                    .build())
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(
                DialogSlot.builder()
                    .withName("slot_name")
                    .withType("slot_type")
                    .withPrompts(DialogSlotPrompt.builder()
                        .withConfirmation("Test")
                        .build())
                    .build()
            ))
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Test")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_SlotConfirmation() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder()
                .addConfirmation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("test_prompt")
                    .build())
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(
                DialogSlot.builder()
                    .withName("slot_name")
                    .withType("slot_type")
                    .withPrompts(DialogSlotPrompt.builder()
                        .withConfirmation("Confirm.Intent-TestIntent.IntentSlot-slot_name")
                        .build())
                    .build()
            ))
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Confirm.Intent-TestIntent.IntentSlot-slot_name")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_SlotConfirmationRequired() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder()
                .withConfirmationRequired(true)
                .addConfirmation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("test_prompt")
                    .build())
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(
                DialogSlot.builder()
                    .withName("slot_name")
                    .withType("slot_type")
                    .withConfirmationRequired(true)
                    .withPrompts(DialogSlotPrompt.builder()
                        .withConfirmation("Confirm.Intent-TestIntent.IntentSlot-slot_name")
                        .build())
                    .build()
            ))
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Confirm.Intent-TestIntent.IntentSlot-slot_name")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_ExplicitSlotElicitPromptId() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder()
                .addElicitation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("test_prompt")
                    .build())
                .withPrompts(DialogSlotPrompt.builder()
                    .withElicitation("Test")
                    .build())
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(
                DialogSlot.builder()
                    .withName("slot_name")
                    .withType("slot_type")
                    .withPrompts(DialogSlotPrompt.builder()
                        .withElicitation("Test")
                        .build())
                    .build()
            ))
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Test")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_SlotElicitation() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder()
                .addElicitation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("test_prompt")
                    .build())
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(
                DialogSlot.builder()
                    .withName("slot_name")
                    .withType("slot_type")
                    .withPrompts(DialogSlotPrompt.builder()
                        .withElicitation("Elicit.Intent-TestIntent.IntentSlot-slot_name")
                        .build())
                    .build()
            ))
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Elicit.Intent-TestIntent.IntentSlot-slot_name")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }

    @Test
    public void testRenderDialogIntent_SlotElicitation_Required() {
        IntentData intentData = IntentData.builder()
            .addSlot("slot_name", IntentSlotData.builder()
                .withElicitationRequired(true)
                .addElicitation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue("test_prompt")
                    .build())
                .build())
            .build();


        DialogIntent expected = DialogIntent.builder()
            .withName("TestIntent")
            .withSlots(Collections.singletonList(
                DialogSlot.builder()
                    .withName("slot_name")
                    .withType("slot_type")
                    .withElicitationRequired(true)
                    .withPrompts(DialogSlotPrompt.builder()
                        .withElicitation("Elicit.Intent-TestIntent.IntentSlot-slot_name")
                        .build())
                    .build()
            ))
            .build();

        DialogIntent actual = underTest.renderDialogIntent(intent, intentData).get();

        assertEquals(expected, actual);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Elicit.Intent-TestIntent.IntentSlot-slot_name")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_prompt")
                .build()))
            .build();

        assertEquals(Collections.singletonList(expectedPrompt), underTest.renderPrompts(intent, intentData));
    }
}
