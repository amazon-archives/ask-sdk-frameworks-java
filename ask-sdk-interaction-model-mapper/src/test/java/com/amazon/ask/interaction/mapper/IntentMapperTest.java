/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.amazon.ask.model.slu.entityresolution.Resolutions;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.stubs.*;
import com.amazon.ask.interaction.types.intent.*;
import com.amazon.ask.interaction.types.slot.AmazonDuration;
import com.amazon.ask.interaction.types.slot.AmazonLiteral;
import com.amazon.ask.interaction.types.slot.AmazonNumber;
import com.amazon.ask.interaction.types.slot.date.AmazonDate;
import com.amazon.ask.interaction.types.slot.date.SpecificDate;
import com.amazon.ask.interaction.types.slot.list.Actor;
import com.amazon.ask.interaction.types.slot.list.DayOfWeek;
import com.amazon.ask.interaction.types.slot.time.AbsoluteTime;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;

public class IntentMapperTest {

    @Test
    public void testStandardIntent() throws IntentParseException, IllegalAccessException, InstantiationException {
        for (Class<? extends StandardIntent> intent : standardIntents) {
            IntentMapper intentMapper = IntentMapper.builder().withModel(Model.builder().intent(intent).build()).build();
            IntentRequest request = makeRequest("AMAZON." + intent.getSimpleName());
            assertEquals(intent.newInstance(), intentMapper.parseIntent(request));
            // Assert that all standard intents other than this one are unknown
            for (Class<? extends StandardIntent> missingIntent : standardIntents) {
                if (missingIntent == intent) continue;

                try {
                    intentMapper.parseIntent(makeRequest("AMAZON." + missingIntent.getSimpleName()));
                    fail("Expected " + UnrecognizedIntentException.class.getName());
                } catch (UnrecognizedIntentException ex) {}
            }
        }
    }

    @Test
    public void testAllStandardIntents() throws IntentParseException, IllegalAccessException, InstantiationException {
        Model.Builder builder = Model.builder();
        for (Class<? extends StandardIntent> intent : standardIntents) {
            builder.intent(intent);
        }
        IntentMapper intentMapper = IntentMapper.builder().withModel(builder.build()).build();
        for (Class<? extends StandardIntent> intent : standardIntents) {
            assertEquals(intent.newInstance(), intentMapper.parseIntent(makeRequest(intent)));
        }
    }

    @Test
    public void testLiteralSlotType() throws IntentParseException {
        AmazonLiteral amazonLiteral = new AmazonLiteral();
        amazonLiteral.setSlot(slot("literalSlot", "test"));
        testSingleSlot("literalSlot", "test", amazonLiteral, TestIntent::getLiteralSlot);
    }

    @Test
    public void testNumberSlotType() throws IntentParseException {
        testSingleSlot("numberSlot", "1", new AmazonNumber(slot("numberSlot", "1"), 1), TestIntent::getNumberSlot);
    }

    @Test
    public void testCustomSlotEnum() throws IntentParseException {
        testSingleSlot("testCustomEnumSlot", "A", TestCustomEnum.A, TestIntent::getTestCustomEnumSlot);
        testSingleSlot("testCustomEnumSlot", "B", TestCustomEnum.B, TestIntent::getTestCustomEnumSlot);
    }

    @Test
    public void testListTypeSlot() throws IntentParseException {
        Actor actor = new Actor();
        actor.setSlot(slot("listTypeSlot", "sam"));
        testSingleSlot("listTypeSlot", "sam", actor, TestIntent::getListTypeSlot);
    }

    @Test
    public void testListTypeDayOfWeekSlot() throws IntentParseException {
        testSingleSlot("dayOfWeekSlot", "monday", new DayOfWeek(slot("dayOfWeekSlot", "monday"), java.time.DayOfWeek.MONDAY), TestIntent::getDayOfWeekSlot);
    }

    @Test
    public void testDateSlot() throws IntentParseException {
        testSingleSlot("dateSlot", "2017-12-25",
                new SpecificDate(slot("dateSlot", "2017-12-25"),
                LocalDate.of(2017, 12, 25)
            ), TestIntent::getDateSlot);
    }

    @Test
    public void testDurationSlot() throws IntentParseException {
        testSingleSlot("durationSlot", "PT1H",
            new AmazonDuration(slot("durationSlot", "PT1H"),
            Period.ZERO, Duration.ofHours(1)), TestIntent::getDurationSlot);
    }

    @Test
    public void testTimeSlot() throws IntentParseException {
        testSingleSlot("timeSlot", "23:30", new AbsoluteTime(slot("timeSlot", "23:30"), LocalTime.of(23, 30)), TestIntent::getTimeSlot);
    }

    @Test
    public void testRawSlot() throws IntentParseException {
        testSingleSlot("slot", "value", slot("slot", "value"), TestIntent::getSlot);
    }

    @Test
    public void testCustomSlot() throws IntentParseException {
        TestCustom expected = new TestCustom();
        expected.setName("testCustomSlot");
        expected.setValue("value");
        expected.setResolutions(Resolutions.builder()
            .withResolutionsPerAuthority(Collections.emptyList())
            .build());
        expected.setExplicitResolutions(Resolutions.builder()
            .withResolutionsPerAuthority(Collections.emptyList())
            .build());
        expected.setSlot(slot("testCustomSlot", "value"));
        expected.setIgnoredSlot(null);
        expected.setExplicitSlot(slot("testCustomSlot", "value"));
        expected.setSlotConfirmationStatus(SlotConfirmationStatus.CONFIRMED);
        expected.setExplicitSlotConfirmationStatus(SlotConfirmationStatus.CONFIRMED);

        testSingleSlot("testCustomSlot", "value", expected, TestIntent::getTestCustomSlot);
    }

    private static <T> void testSingleSlot(String name, String value, T expected, Function<TestIntent, T> get) throws IntentParseException {
        IntentMapper intentMapper = IntentMapper.fromModel(Model.builder().intent(TestIntent.class).build());

        Map<String, String> values = new HashMap<>();
        // default values
        values.put("dayOfWeekSlot", null);
        values.put("durationSlot", null);
        values.put("literalSlot", null);
        values.put("dateSlot", null);
        values.put("timeSlot", null);
        values.put("testCustomSlot", null);
        values.put("testCustomEnumSlot", null);
        values.put("numberSlot", null);
        values.put("listTypeSlot", null);
        values.put("slot", null);
        // overwrite value under test
        values.put(name, value);
        IntentRequest request = makeRequest("TestIntent", values);

        TestIntent actual = (TestIntent) intentMapper.parseIntent(request);
        assertEquals(expected, get.apply(actual));
//        assertEquals(expected, intentMapper.parseIntentSlot(request, name));
    }

    private static com.amazon.ask.model.Slot slot(String name, String value) {
        return Slot.builder()
            .withName(name)
            .withValue(value)
            .withResolutions(Resolutions.builder()
                .withResolutionsPerAuthority(Collections.emptyList())
                .build())
            .withConfirmationStatus(SlotConfirmationStatus.CONFIRMED)
            .build();
    }

    @Test(expected = UnrecognizedIntentException.class)
    public void testUnrecognizedIntent() throws IntentParseException {
        IntentMapper intentMapper = IntentMapper.fromModel(Model.builder().build());

        intentMapper.parseIntentSlot(makeRequest("missing"), "slot");
    }

    @Test(expected = UnrecognizedSlotException.class)
    public void testNullSlots() throws IntentParseException {
        IntentMapper intentMapper = IntentMapper.fromModel(Model.builder().intent(StopIntent.class).build());

        intentMapper.parseIntentSlot(makeRequest("AMAZON.StopIntent"), "slot");
    }

    @Test(expected = UnrecognizedSlotException.class)
    public void testUnrecognizedSlot() throws IntentParseException {
        IntentMapper intentMapper = IntentMapper.fromModel(Model.builder().intent(StopIntent.class).build());

        intentMapper.parseIntentSlot(makeRequest("AMAZON.StopIntent", Collections.singletonMap("not-slot", "test")), "slot");
    }

    @Test
    public void testNullSlotValue() throws IntentParseException {
        IntentMapper intentMapper = IntentMapper.fromModel(Model.builder().intent(TestNullSlot.class).build());

        IntentRequest intentRequest = IntentRequest.builder()
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("TestNullSlot")
                .withSlots(Collections.singletonMap(
                    "slot",
                    com.amazon.ask.model.Slot.builder()
                        .withName("slot")
                        .withValue(null)
                        .build()
                ))
                .build())
            .build();

        assertNull(intentMapper.parseIntentSlot(intentRequest, "slot"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedTypeFailure() {
        Model.builder().intent(TestIntentUnsupportedType.class).build();
    }

    private static <T extends StandardIntent> IntentRequest makeRequest(Class<T> clazz) {
        return makeRequest("AMAZON." + clazz.getSimpleName(), Collections.emptyMap());
    }
    private static IntentRequest makeRequest(String intentName) {
        return makeRequest(intentName, Collections.emptyMap());
    }
    private static IntentRequest makeRequest(String intentName, Map<String, String> slots) {
        com.amazon.ask.model.Intent.Builder intentBuilder = com.amazon.ask.model.Intent.builder().withName(intentName);

        Map<String, Slot> slotsValues = new HashMap<>();
        for (Map.Entry<String, String> entry : slots.entrySet()) {
            slotsValues.put(entry.getKey(), Slot.builder()
                .withName(entry.getKey())
                .withValue(entry.getValue())
                .withResolutions(Resolutions.builder()
                    .withResolutionsPerAuthority(Collections.emptyList())
                    .build())
                .withConfirmationStatus(SlotConfirmationStatus.CONFIRMED)
                .build());
        }
        intentBuilder.withSlots(slotsValues);

        return IntentRequest.builder()
            .withRequestId("requestId")
            .withIntent(intentBuilder.build())
            .build();
    }

    private static final List<Class<? extends StandardIntent>> standardIntents = Arrays.asList(
        CancelIntent.class,
        FallbackIntent.class,
        HelpIntent.class,
        LoopOffIntent.class,
        LoopOnIntent.class,
        MoreIntent.class,
        NavigateHomeIntent.class,
        NavigateSettingsIntent.class,
        NextIntent.class,
        NoIntent.class,
        PageDownIntent.class,
        PageUpIntent.class,
        PauseIntent.class,
        PreviousIntent.class,
        RepeatIntent.class,
        ResumeIntent.class,
        ScrollDownIntent.class,
        ScrollLeftIntent.class,
        ScrollRightIntent.class,
        ScrollUpIntent.class,
        ShuffleOffIntent.class,
        ShuffleOnIntent.class,
        StartOverIntent.class,
        StopIntent.class,
        YesIntent.class
    );

    @Intent
    public static class TestNullSlot {
        @SlotProperty
        private AmazonDate slot;

        public AmazonDate getSlot() {
            return slot;
        }

        public void setSlot(AmazonDate slot) {
            this.slot = slot;
        }
    }
}
