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
import com.amazon.ask.interaction.mapper.slot.FourDigitNumberParser;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.amazon.ask.interaction.types.slot.FourDigitNumber;
import org.junit.Test;

import static org.junit.Assert.*;

public class FourDigitNumberParserTest {
    private static final SlotPropertyReader<FourDigitNumber> parser = new FourDigitNumberParser();
    private static Slot slot(String value) {
        return Slot.builder().withName("test").withValue(value).build();
    }

    @Test
    public void testParse() throws IntentParseException {
        assertEquals(new FourDigitNumber(slot("1234"), 1, 2, 3, 4), parser.read(null, slot("1234")));
    }

    @Test(expected = IntentParseException.class)
    public void testTooManyDigits() throws IntentParseException {
        parser.read(null, slot("12345"));
    }

    @Test(expected = IntentParseException.class)
    public void testTooFewDigits() throws IntentParseException {
        parser.read(null, slot("12345"));
    }

    @Test(expected = IntentParseException.class)
    public void testNaN() throws IntentParseException {
        parser.read(null, slot("NaN"));
    }

    @Test
    public void testDigits() {
        FourDigitNumber number = new FourDigitNumber(slot("0123"), 0, 1, 2, 3);
        assertEquals(0, number.getFirstDigit());
        assertEquals(1, number.getSecondDigit());
        assertEquals(2, number.getThirdDigit());
        assertEquals(3, number.getFourthDigit());
        assertArrayEquals(number.getDigits(), new int[]{0,1,2,3});
    }

    @Test
    public void testIntValue() {
        assertEquals(new FourDigitNumber(slot("9999"), 9,9,9,9).getNumber(), 9999);
        assertEquals(new FourDigitNumber(slot("1234"), 1,2,3,4).getNumber(), 1234);
        assertEquals(new FourDigitNumber(slot("0123"), 0,1,2,3).getNumber(), 123);
        assertEquals(new FourDigitNumber(slot("0012"), 0,0,1,2).getNumber(), 12);
        assertEquals(new FourDigitNumber(slot("0001"), 0,0,0,1).getNumber(), 1);
        assertEquals(new FourDigitNumber(slot("0000"), 0,0,0,0).getNumber(), 0);
    }

    @Test
    public void testStringValue() {
        assertEquals(new FourDigitNumber(slot("9999"), 9,9,9,9).stringValue(), "9999");
        assertEquals(new FourDigitNumber(slot("1234"), 1,2,3,4).stringValue(), "1234");
        assertEquals(new FourDigitNumber(slot("0123"), 0,1,2,3).stringValue(), "0123");
        assertEquals(new FourDigitNumber(slot("0012"), 0,0,1,2).stringValue(), "0012");
        assertEquals(new FourDigitNumber(slot("0001"), 0,0,0,1).stringValue(), "0001");
        assertEquals(new FourDigitNumber(slot("0000"), 0,0,0,0).stringValue(), "0000");
    }

    @Test
    public void testEquals() {
        FourDigitNumber a = new FourDigitNumber(slot("9999"), 9,9,9,9);
        FourDigitNumber aCopy = new FourDigitNumber(slot("9999"),9,9,9,9);
        FourDigitNumber b = new FourDigitNumber(slot("0000"),0,0,0,0);

        assertTrue(a.equals(a));
        assertTrue(a.equals(aCopy));
        assertFalse(a.equals(b));
        assertFalse(a.equals(null));
        assertFalse(a.equals("string"));
    }
}
