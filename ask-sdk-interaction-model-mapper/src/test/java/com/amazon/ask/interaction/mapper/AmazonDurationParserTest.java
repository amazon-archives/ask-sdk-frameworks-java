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
import com.amazon.ask.interaction.mapper.slot.AmazonDurationParser;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.amazon.ask.interaction.types.slot.AmazonDuration;
import org.junit.Test;

import java.time.Duration;
import java.time.Period;

import static org.junit.Assert.assertEquals;

public class AmazonDurationParserTest {
    private static final SlotPropertyReader<AmazonDuration> parser = new AmazonDurationParser();
    private static Slot slot(String value) {
        return Slot.builder().withName("test").withValue(value).build();
    }

    @Test
    public void testPeriodAndDuration() throws IntentParseException {
        assertEquals(new AmazonDuration(slot("P1DT1H"), Period.ofDays(1), Duration.ofHours(1)), parser.read(null, slot("P1DT1H")));
    }

    @Test
    public void testDurationOnly() throws IntentParseException {
        assertEquals(new AmazonDuration(slot("PT1H"), Period.ZERO, Duration.ofHours(1)), parser.read(null, slot("PT1H")));
    }

    @Test
    public void testPeriodOnly() throws IntentParseException {
        assertEquals(new AmazonDuration(slot("P1D"), Period.ofDays(1), Duration.ZERO), parser.read(null, slot("P1D")));
    }

    @Test(expected = IntentParseException.class)
    public void testUnmatchedFormat() throws IntentParseException {
        parser.read(null, slot("1T2T3"));
    }

    @Test(expected = IntentParseException.class)
    public void testInvalidPeriod() throws IntentParseException {
        parser.read(null, slot("PINVALIDT0H"));
    }

    @Test(expected = IntentParseException.class)
    public void testInvalidPeriodOnly() throws IntentParseException {
        parser.read(null, slot("PINVALID"));
    }

    @Test(expected = IntentParseException.class)
    public void testInvalidDuration() throws IntentParseException {
        parser.read(null, slot("PTINVALIDH"));
    }
}
