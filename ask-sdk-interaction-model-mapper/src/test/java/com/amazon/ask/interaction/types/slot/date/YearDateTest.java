/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class YearDateTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new YearDate(null, 1);
    }

    @Test
    public void testYear() {
        new YearDate(mockSlot, 0);
        new YearDate(mockSlot, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeYear() {
        new YearDate(mockSlot, -1);
    }

    @Test
    public void testEqualsSelf() {
        YearDate date = new YearDate(mockSlot, 1);

        assertEquals(date, date);
        assertEquals(date.getYear(), date.getYear());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        YearDate date1 = new YearDate(mockSlot, 1);
        YearDate date2 = new YearDate(mockSlot, 1);

        assertEquals(date1, date2);
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        YearDate date1 = new YearDate(mockSlot, 1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        YearDate date1 = new YearDate(mockSlot, 1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        YearDate date1 = new YearDate(mockSlot, 1);
        YearDate date2 = new YearDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getYear(), date2.getYear());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentYear() {
        YearDate date1 = new YearDate(mockSlot, 1);
        YearDate date2 = new YearDate(mockSlot, 2);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
