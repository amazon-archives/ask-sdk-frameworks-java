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
import com.amazon.ask.interaction.mapper.slot.AmazonDateParser;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.amazon.ask.interaction.types.slot.date.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AmazonDateParserTest {
    private static final SlotPropertyReader<AmazonDate> parser = new AmazonDateParser();
    private static Slot slot(String value) {
        return Slot.builder().withName("test").withValue(value).build();
    }

    @Test(expected = SlotValueParseException.class)
    public void testUnknownPattern() throws IntentParseException {
        parser.read(null, slot("unknown"));
    }

    @Test
    public void testPresentRef() throws IntentParseException {
        AmazonDate date = parser.read(null, slot("PRESENT_REF"));
        assertTrue(date instanceof PresentRef);
    }

    @Test
    public void testISO8601() throws IntentParseException {
        Slot slot = slot("2017-12-25");
        assertEquals(new SpecificDate(slot, LocalDate.of(2017, 12, 25)), parser.read(null, slot));
    }

    @Test(expected = SlotValueParseException.class)
    public void testISO8601InvalidDate() throws IntentParseException {
        parser.read(null, slot("2017-13-25"));
    }

    @Test
    public void testWeek1() throws IntentParseException {
        Slot slot = slot("2017-W01");
        assertEquals(new WeekDate(slot, 2017, 1), parser.read(null, slot));
    }

    @Test
    public void testWeek52() throws IntentParseException {
        Slot slot = slot("2017-W52");
        assertEquals(new WeekDate(slot, 2017, 52), parser.read(null, slot));
    }

    @Test(expected = IntentParseException.class)
    public void testWeek53() throws IntentParseException {
        Slot slot = slot("2017-W53");
        assertEquals(new WeekDate(slot, 2017, 1), parser.read(null, slot));
    }

    @Test
    public void testWeekend1() throws IntentParseException {
        Slot slot = slot("2017-W01-WE");
        assertEquals(new WeekendDate(slot, 2017, 1), parser.read(null, slot));
    }

    @Test
    public void testWeekend52() throws IntentParseException {
        Slot slot = slot("2017-W52-WE");
        assertEquals(new WeekendDate(slot, 2017, 52), parser.read(null, slot));
    }

    @Test(expected = IntentParseException.class)
    public void testWeekend53() throws IntentParseException {
        Slot slot = slot("2017-W53-WE");
        assertEquals(new WeekendDate(slot, 2017, 1), parser.read(null, slot));
    }

    @Test
    public void testMonth1() throws IntentParseException {
        Slot slot = slot("2017-01");
        assertEquals(new MonthDate(slot, 2017, 1), parser.read(null, slot));
    }

    @Test
    public void testMonth12() throws IntentParseException {
        Slot slot = slot("2017-12");
        assertEquals(new MonthDate(slot, 2017, 12), parser.read(null, slot));
    }

    @Test(expected = IntentParseException.class)
    public void testMonth13() throws IntentParseException {
        parser.read(null, slot("2017-13"));
    }

    @Test
    public void testYear() throws IntentParseException {
        Slot slot = slot("2017");
        assertEquals(new YearDate(slot, 2017), parser.read(null, slot));
    }

    @Test
    public void testDecade() throws IntentParseException {
        assertEquals(new DecadeDate(slot("201X"), 20, 1), parser.read(null, slot("201X")));
        assertEquals(new DecadeDate(slot("200X"), 20, 0), parser.read(null, slot("200X")));
        assertEquals(new DecadeDate(slot("209X"), 20, 9), parser.read(null, slot("209X")));
    }

    @Test
    public void testSeasons() throws IntentParseException {
        assertEquals(new SeasonDate(slot("2017-FA"), 2017, Season.FA), parser.read(null, slot("2017-FA")));
        assertEquals(new SeasonDate(slot("2017-SP"), 2017, Season.SP), parser.read(null, slot("2017-SP")));
        assertEquals(new SeasonDate(slot("2017-SU"), 2017, Season.SU), parser.read(null, slot("2017-SU")));
        assertEquals(new SeasonDate(slot("2017-WI"), 2017, Season.WI), parser.read(null, slot("2017-WI")));
    }
}
