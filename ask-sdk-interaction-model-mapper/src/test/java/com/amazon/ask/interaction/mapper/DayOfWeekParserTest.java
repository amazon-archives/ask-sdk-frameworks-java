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

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.slot.DayOfWeekParser;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.amazon.ask.interaction.types.slot.list.DayOfWeek;
import org.junit.Test;

import static java.time.DayOfWeek.*;
import static java.time.DayOfWeek.SUNDAY;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DayOfWeekParserTest {
    @Test
    public void testDayOfWeek() throws IntentParseException {
        SlotPropertyReader<DayOfWeek> parser = new DayOfWeekParser();

        assertEquals(new DayOfWeek(slot("monday"), MONDAY), parser.read(null, slot("monday")));
        assertEquals(new DayOfWeek(slot("tuesday"), TUESDAY), parser.read(null, slot("tuesday")));
        assertEquals(new DayOfWeek(slot("wednesday"), WEDNESDAY), parser.read(null, slot("wednesday")));
        assertEquals(new DayOfWeek(slot("thursday"), THURSDAY), parser.read(null, slot("thursday")));
        assertEquals(new DayOfWeek(slot("friday"), FRIDAY), parser.read(null, slot("friday")));
        assertEquals(new DayOfWeek(slot("saturday"), SATURDAY), parser.read(null, slot("saturday")));
        assertEquals(new DayOfWeek(slot("sunday"), SUNDAY), parser.read(null, slot("sunday")));

        assertEquals(new DayOfWeek(slot("MONDAY"), MONDAY), parser.read(null, slot("MONDAY")));
        assertEquals(new DayOfWeek(slot("TUESDAY"), TUESDAY), parser.read(null, slot("TUESDAY")));
        assertEquals(new DayOfWeek(slot("WEDNESDAY"), WEDNESDAY), parser.read(null, slot("WEDNESDAY")));
        assertEquals(new DayOfWeek(slot("THURSDAY"), THURSDAY), parser.read(null, slot("THURSDAY")));
        assertEquals(new DayOfWeek(slot("FRIDAY"), FRIDAY), parser.read(null, slot("FRIDAY")));
        assertEquals(new DayOfWeek(slot("SATURDAY"), SATURDAY), parser.read(null, slot("SATURDAY")));
        assertEquals(new DayOfWeek(slot("SUNDAY"), SUNDAY), parser.read(null, slot("SUNDAY")));
    }

    @Test(expected = IntentParseException.class)
    public void testDayOfWeekInvalid() throws IntentParseException {
        new DayOfWeekParser().read(null, slot("invalid"));
    }

    private static Slot slot(String value) {
        return Slot.builder().withName("test").withValue(value).build();
    }
}
