/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data.model;

import com.amazon.ask.interaction.model.*;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SubModelTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullLanguageModel() {
        SubModel.builder().withLanguageModel(null).build();
    }

    @Test
    public void testEqualsSelf() {
        SubModel model = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .build())
            .build();

        assertEquals(model, model);
        assertEquals(model.hashCode(), model.hashCode());
    }

    @Test
    public void testNotEqualsNull() {
        SubModel model = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .build())
            .build();

        assertNotEquals(model, null);
    }

    @Test
    public void testNotEqualsWrongClass() {
        SubModel model = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .build())
            .build();

        assertNotEquals(model, "wrong class");
    }

    @Test
    public void testEquals() {
        SubModel left = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        SubModel right = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testDifferentLanguageModel() {
        SubModel left = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        SubModel right = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("different")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testDifferentDialog() {
        SubModel left = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        SubModel right = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("different")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNullDialog() {
        SubModel left = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        SubModel right = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(null)
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testDifferentPrompts() {
        SubModel left = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        SubModel right = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("different")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNullPrompts() {
        SubModel left = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .addPrompts(Collections.singletonList(
                Prompt.builder()
                    .withId("test")
                    .withVariations(Collections.singletonList(
                        PromptVariation.builder()
                            .withType("PlainText")
                            .withValue("test")
                            .build()
                    ))
                    .build()
            ))
            .build();

        SubModel right = SubModel.builder()
            .withLanguageModel(SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName("test")
                        .withSamples(Collections.singletonList("test"))
                        .withSlots(Collections.singletonList(
                            Slot.builder()
                                .withName("test")
                                .withType("test")
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withDialog(Dialog.builder()
                .withIntents(Collections.singletonList(
                    DialogIntent.builder()
                        .withName("test")
                        .withConfirmationRequired(true)
                        .withPrompts(DialogIntentPrompt.builder()
                            .withConfirmation("test")
                            .build())
                        .withSlots(Collections.singletonList(
                            DialogSlot.builder()
                                .withName("test")
                                .withType("test")
                                .withConfirmationRequired(true)
                                .withElicitationRequired(true)
                                .withPrompts(DialogSlotPrompt.builder()
                                    .withConfirmation("testConfirm")
                                    .withElicitation("testElicit")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build())
            .withPrompts(null)
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }
}
