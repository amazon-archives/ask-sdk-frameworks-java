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
import com.amazon.ask.interaction.Locales;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.interaction.renderer.models.TestIntent;
import com.amazon.ask.interaction.renderer.models.TestIntentInherit;
import com.amazon.ask.interaction.renderer.models.TestPrompts;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.StandardIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static com.amazon.ask.interaction.Utils.stringifyLocale;
import static org.junit.Assert.assertEquals;

public class InteractionModelRendererTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();

    private final InteractionModelRenderer underTest = new InteractionModelRenderer();

    @Test
    public void testStandardIntentsLanguageModel() throws IOException {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "test")
            .addModel(Model.builder()
                .intent(HelpIntent.class)
                .intent(StopIntent.class)
                .build())
            .build();

        LanguageModel actual = underTest.render(skill, Locales.en_US).getLanguageModel();
        LanguageModel expected = LanguageModel.builder()
            .withInvocationName("test")
            .withIntents(Arrays.asList(
                makeIntent(HelpIntent.class, null, null),
                makeIntent(StopIntent.class, null, null)
            ))
            .withTypes(Collections.emptyList())
            .build();

        assertEquals(WRITER.writeValueAsString(expected), WRITER.writeValueAsString(actual));
        assertEquals(2, actual.getIntents().size());
    }

    @Test
    public void testCustomIntentsLanguageModel() throws IOException {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "test")
            .addModel(Model.builder()
                .intent(TestIntent.class)
                .build())
            .build();

        LanguageModel actual = underTest.render(skill, Locales.en_US).getLanguageModel();
        LanguageModel expected = LanguageModel.builder()
            .withInvocationName("test")
            .withIntents(Collections.singletonList(
                makeIntent("TestIntent", Arrays.asList(
                    makeIntentSlot("customSlot", "TestCustom"),
                    makeIntentSlot("dateSlot", "AMAZON.DATE"),
                    makeIntentSlot("dayOfWeekSlot", "AMAZON.DayOfWeek"),
                    makeIntentSlot("durationSlot", "AMAZON.DURATION"),
                    makeIntentSlot("listTypeSlot", "AMAZON.Actor"),
                    makeIntentSlot("literalSlot", "AMAZON.LITERAL"),
                    makeIntentSlot("numberSlot", "AMAZON.NUMBER"),
                    makeIntentSlot("timeSlot", "AMAZON.TIME")
                ), Collections.singletonList("test_en_US"))))
            .withTypes(Collections.singletonList(
                makeSlotType("TestCustom", Arrays.asList(
                    makeSlotTypeValue("A", Collections.singletonList("test_A_en_US")),
                    makeSlotTypeValue("B", Collections.singletonList("test_B_en_US"))))))
            .build();

        assertEquals(WRITER.writeValueAsString(expected), WRITER.writeValueAsString(actual));
    }

    @Test
    public void testSynonyms() throws IOException {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .withInvocationName(Locales.en_GB, "en_GB")
            .withInvocationName(Locales.de_DE, "de_DE")
            .withInvocationName(Locales.fr_FR, "fr_FR")
            .addModel(Model.builder()
                .intent(TestIntent.class)
                .build())
            .build();

        for (Locale locale : Locales.values()) {
            Collection<SlotType> actual = underTest.render(skill, locale).getLanguageModel().getTypes();

            List<SlotType> expected = Collections.singletonList(
                makeSlotType("TestCustom", Arrays.asList(
                    makeSlotTypeValue("A", Collections.singletonList("test_A_" + stringifyLocale(locale))),
                    makeSlotTypeValue("B", Collections.singletonList("test_B_" + stringifyLocale(locale)))
                    )
                ));

            assertEquals(WRITER.writeValueAsString(expected), WRITER.writeValueAsString(actual));
        }
    }

    @Test
    public void testSamples() throws IOException {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .withInvocationName(Locales.en_GB, "en_GB")
            .withInvocationName(Locales.de_DE, "de_DE")
            .withInvocationName(Locales.fr_FR, "fr_FR")
            .addModel(Model.builder()
                .intent(TestIntent.class)
                .build())
            .build();

        for (Locale locale : Locales.values()) {
            Collection<com.amazon.ask.interaction.model.Intent> actual = underTest.render(skill, locale).getLanguageModel().getIntents();

            List<com.amazon.ask.interaction.model.Intent> expected = Collections.singletonList(
                makeIntent("TestIntent", Arrays.asList(
                    makeIntentSlot("customSlot", "TestCustom"),
                    makeIntentSlot("dateSlot", "AMAZON.DATE"),
                    makeIntentSlot("dayOfWeekSlot", "AMAZON.DayOfWeek"),
                    makeIntentSlot("durationSlot", "AMAZON.DURATION"),
                    makeIntentSlot("listTypeSlot", "AMAZON.Actor"),
                    makeIntentSlot("literalSlot", "AMAZON.LITERAL"),
                    makeIntentSlot("numberSlot", "AMAZON.NUMBER"),
                    makeIntentSlot("timeSlot", "AMAZON.TIME")
                ), Collections.singletonList("test_" + stringifyLocale(locale)))
            );

            assertEquals(WRITER.writeValueAsString(expected), WRITER.writeValueAsString(actual));
        }
    }

    @Test
    public void testIntentConfirmation() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(TestIntent.class)
                .build())
            .build();

        InteractionModel model = underTest.render(skill, Locales.en_US);

        Prompt expectedPrompt = Prompt.builder()
            .withId("Confirm.Intent-TestIntent")
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_en_US confirmation")
                .build()))
            .build();
        Prompt actualPrompt = model.getPrompts().stream().filter(p -> p.getId().equals("Confirm.Intent-TestIntent")).findFirst().get();
        assertEquals(actualPrompt, expectedPrompt);
        DialogIntent actualIntent = model.getDialog().getIntents().stream().filter(d -> d.getName().equals("TestIntent")).findFirst().get();
        assertEquals(actualIntent.getPrompts().getConfirmation(), "Confirm.Intent-TestIntent");
        assertEquals(actualIntent.getConfirmationRequired(), true);
    }

    @Test
    public void testIntentExplicitConfirmationPromptId() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(TestPrompts.class)
                .build())
            .build();

        InteractionModel model = underTest.render(skill, Locales.en_US);

        Prompt expectedPrompt = Prompt.builder()
            .withId("testIntentConfirmation") // explicit id in json file
            .withVariations(Collections.singletonList(PromptVariation.builder()
                .withType("PlainText")
                .withValue("test_en_US confirmation")
                .build()))
            .build();
        Prompt actualPrompt = model.getPrompts().stream().filter(p -> p.getId().equals("testIntentConfirmation")).findFirst().get();
        assertEquals(actualPrompt, expectedPrompt);
        DialogIntent actualIntent = model.getDialog().getIntents().stream().filter(d -> d.getName().equals("TestPrompts")).findFirst().get();
        assertEquals(actualIntent.getPrompts().getConfirmation(), "testIntentConfirmation"); // explicit
        assertEquals(actualIntent.getConfirmationRequired(), true);
    }

    @Test
    public void testSlotConfirmation() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(TestIntent.class)
                .build())
            .build();

        InteractionModel model = underTest.render(skill, Locales.en_US);

        DialogSlot slot = model.getDialog().getIntents().get(0).getSlots().stream()
            .filter(s -> s.getName().equals("customSlot"))
            .findFirst().get();

        assertEquals(slot.getType(), "TestCustom");
        assertEquals(slot.getConfirmationRequired(), true);
        assertEquals(slot.getPrompts().getConfirmation(), "Confirm.Intent-TestIntent.IntentSlot-customSlot");

        assertEquals(
            model.getPrompts().stream().filter(p -> p.getId().equals("Confirm.Intent-TestIntent.IntentSlot-customSlot")).findFirst().get().getVariations(),
            Collections.singletonList(PromptVariation.builder().withType("PlainText").withValue("test_en_US customSlot confirmation").build()));
    }

    @Test
    public void testSlotElicitation() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(TestIntent.class)
                .build())
            .build();

        InteractionModel model = underTest.render(skill, Locales.en_US);

        DialogSlot slot = model.getDialog().getIntents().get(0).getSlots().stream()
            .filter(s -> s.getName().equals("customSlot"))
            .findFirst().get();

        assertEquals(slot.getType(), "TestCustom");
        assertEquals(slot.getElicitationRequired(), true);
        assertEquals(slot.getPrompts().getElicitation(), "Elicit.Intent-TestIntent.IntentSlot-customSlot");

        assertEquals(
            model.getPrompts().stream().filter(p -> p.getId().equals("Elicit.Intent-TestIntent.IntentSlot-customSlot")).findFirst().get().getVariations(),
            Collections.singletonList(PromptVariation.builder().withType("PlainText").withValue("test_en_US customSlot elicitation").build()));
    }

    @Test
    public void testSlotExplicitPromptIds() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(TestPrompts.class)
                .build())
            .build();

        InteractionModel model = underTest.render(skill, Locales.en_US);

        DialogSlot slot = model.getDialog().getIntents().get(0).getSlots().stream()
            .filter(s -> s.getName().equals("customSlot"))
            .findFirst().get();

        assertEquals(slot.getPrompts().getConfirmation(), "customSlot.confirmation"); // explicit prompt
        assertEquals(slot.getPrompts().getElicitation(), "customSlot.elicitation"); // explicit prompt

        assertEquals(
            model.getPrompts().stream().filter(p -> p.getId().equals("customSlot.confirmation")).findFirst().get().getVariations(),
            Collections.singletonList(PromptVariation.builder().withType("PlainText").withValue("test_en_US customSlot confirmation").build()));
        assertEquals(
            model.getPrompts().stream().filter(p -> p.getId().equals("customSlot.elicitation")).findFirst().get().getVariations(),
            Collections.singletonList(PromptVariation.builder().withType("PlainText").withValue("test_en_US customSlot elicitation").build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingInvocationName() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .build();

        underTest.render(skill, Locales.de_DE);
    }

    @Test
    public void testRepeatedIncludes() {
        SkillModel skill = SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(TestIntentInherit.class)
                .build())
            .build();

        InteractionModel model = underTest.render(skill, Locales.en_US);

        Intent intent = model.getLanguageModel().getIntents().stream().findFirst().get();

        assertEquals(Arrays.asList("merged", "merged2", "test_en_US"), intent.getSamples());
    }

    private static com.amazon.ask.interaction.model.Intent makeIntent(String name, List<com.amazon.ask.interaction.model.Slot> slots, List<String> samples) {
        return com.amazon.ask.interaction.model.Intent.builder()
            .withName(name)
            .withSlots(slots)
            .withSamples(samples)
            .build();
    }
    private static <T extends StandardIntent> com.amazon.ask.interaction.model.Intent makeIntent(Class<T> clazz, List<com.amazon.ask.interaction.model.Slot> slots, List<String> samples) {
        return makeIntent("AMAZON." + clazz.getSimpleName(), slots, samples);
    }
    private static SlotType makeSlotType(String name, List<SlotTypeValue> values) {
        return SlotType.builder()
            .withName(name)
            .withValues(values)
            .build();
    }
    private static SlotTypeValue makeSlotTypeValue(String id, List<String> synonyms) {
        return SlotTypeValue.builder()
            .withId(id)
            .withName(SlotValue.builder()
                .withValue(id)
                .withSynonyms(synonyms)
                .build())
            .build();
    }
    private static com.amazon.ask.interaction.model.Slot makeIntentSlot(String name, String type) {
        return com.amazon.ask.interaction.model.Slot.builder()
            .withName(name)
            .withType(type)
            .build();
    }
}
