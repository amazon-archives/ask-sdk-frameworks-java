/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.definition;

import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.stubs.*;
import com.amazon.ask.interaction.types.slot.AmazonDuration;
import com.amazon.ask.interaction.types.slot.AmazonLiteral;
import com.amazon.ask.interaction.types.slot.AmazonNumber;
import com.amazon.ask.interaction.types.slot.date.AmazonDate;
import com.amazon.ask.interaction.types.slot.list.Actor;
import com.amazon.ask.interaction.types.slot.list.DayOfWeek;
import com.amazon.ask.interaction.types.slot.time.AmazonTime;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ModelTest {
    @Test
    public void testCustomSlotType() {
        Model model = Model.builder().slotType(TestCustom.class).build();

        SlotTypeDefinition expected = SlotTypeDefinition.builder()
            .withSlotTypeClass(TestCustom.class)
            .withName("TestCustom")
            .withCustom(true)
            .build();

        assertEquals(expected, model.getSlotTypes().get("TestCustom"));
    }

    @Test
    public void testCustomEnumSlotType() {
        Model model = Model.builder().slotType(TestCustomEnum.class).build();

        SlotTypeDefinition expected = SlotTypeDefinition.builder()
            .withSlotTypeClass(TestCustomEnum.class)
            .withName("TestCustomEnum")
            .withCustom(true)
            .build();

        assertEquals(expected, model.getSlotTypes().get("TestCustomEnum"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateSlotTypes() {
        Model.Builder builder = Model.builder();
        builder.slotType(DuplicateSlot1.class);
        builder.slotType(DuplicateSlot2.class);
    }

    @Test
    public void testCustomIntent() {
        Map<String, SlotTypeDefinition> slotTypes = new TreeMap<>();
        slotTypes.put("literalSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.LITERAL")
            .withCustom(false)
            .withSlotTypeClass(AmazonLiteral.class)
            .build());
        slotTypes.put("numberSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.NUMBER")
            .withCustom(false)
            .withSlotTypeClass(AmazonNumber.class)
            .build());
        slotTypes.put("testCustomSlot", SlotTypeDefinition.builder()
            .withName("TestCustom")
            .withCustom(true)
            .withSlotTypeClass(TestCustom.class)
            .build());
        slotTypes.put("testCustomEnumSlot", SlotTypeDefinition.builder()
            .withName("TestCustomEnum")
            .withCustom(true)
            .withSlotTypeClass(TestCustomEnum.class)
            .build());
        slotTypes.put("listTypeSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.Actor")
            .withCustom(false)
            .withSlotTypeClass(Actor.class)
            .build());
        slotTypes.put("dateSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.DATE")
            .withCustom(false)
            .withSlotTypeClass(AmazonDate.class)
            .build());
        slotTypes.put("durationSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.DURATION")
            .withCustom(false)
            .withSlotTypeClass(AmazonDuration.class)
            .build());
        slotTypes.put("timeSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.TIME")
            .withCustom(false)
            .withSlotTypeClass(AmazonTime.class)
            .build());
        slotTypes.put("dayOfWeekSlot", SlotTypeDefinition.builder()
            .withName("AMAZON.DayOfWeek")
            .withCustom(false)
            .withSlotTypeClass(DayOfWeek.class)
            .build());
        slotTypes.put("slot", SlotTypeDefinition.builder()
            .withName("AMAZON.LITERAL")
            .withCustom(false)
            .withSlotTypeClass(AmazonLiteral.class)
            .build());

        IntentDefinition expected = IntentDefinition.builder()
            .withIntentType(TypeFactory.defaultInstance().constructSimpleType(TestIntent.class, new JavaType[]{}))
            .withName("TestIntent")
            .withCustom(true)
            .withSlots(slotTypes)
            .build();

        Model model = Model.builder().intent(TestIntent.class).build();

        assertEquals(expected, model.getIntentDefinitions().get("TestIntent"));
    }

    @Test
    public void testGenericIntent() {
        Model model = Model.builder().genericIntent(TestGenericIntent.class, AmazonLiteral.class).build();

        IntentDefinition expected = IntentDefinition.builder()
            .withName("TestGenericIntent_AMAZON_LITERAL")
            .withCustom(true)
            .withSlots(Collections.singletonMap("slot", SlotTypeDefinition.builder()
                .withName("AMAZON.LITERAL")
                .withSlotTypeClass(AmazonLiteral.class)
                .withCustom(false)
                .build()))
            .withIntentType(TypeFactory.defaultInstance().constructParametricType(TestGenericIntent.class, AmazonLiteral.class))
            .build();

        assertEquals(expected, model.getIntentDefinitions().get("TestGenericIntent_AMAZON_LITERAL"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedSlotType() {
        Model.builder().intent(TestIntentUnsupportedType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateSlots() {
        Model.builder().intent(TestDuplicateSlots.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIntentAnnotation() {
        Model.builder().intent(TestMissingAnnotation.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSlotAnnotation() {
        Model.builder().slotType(TestMissingAnnotation.class);
    }

    public static class TestMissingAnnotation {

    }

    @Intent
    public static class TestDuplicateSlots {
        @SlotProperty
        private AmazonLiteral slot;

        @SlotProperty(name = "slot")
        private AmazonLiteral slot2;

        public AmazonLiteral getSlot() {
            return slot;
        }

        public void setSlot(AmazonLiteral slot) {
            this.slot = slot;
        }

        public AmazonLiteral getSlot2() {
            return slot2;
        }

        public void setSlot2(AmazonLiteral slot2) {
            this.slot2 = slot2;
        }
    }

    @SlotType("DuplicateSlot")
    public static class DuplicateSlot1 {
    }

    @BuiltIn // different
    @SlotType("DuplicateSlot")
    public static class DuplicateSlot2 {
    }
}
