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

public class MonthDateTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new MonthDate(null, 1, 1);
    }

    @Test
    public void testMonth() {
        new MonthDate(mockSlot, 0, 0);
        new MonthDate(mockSlot, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMonth() {
        new MonthDate(mockSlot, 1, -1);
    }

    @Test
    public void testYear() {
        new MonthDate(mockSlot, 0, 1);
        new MonthDate(mockSlot, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeYear() {
        new MonthDate(mockSlot, -1, 1);
    }

    @Test
    public void testEqualsSelf() {
        MonthDate date = new MonthDate(mockSlot, 1, 1);

        assertEquals(date, date);
        assertEquals(date.getMonth(), date.getMonth());
        assertEquals(date.getYear(), date.getYear());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        MonthDate date1 = new MonthDate(mockSlot, 1, 1);
        MonthDate date2 = new MonthDate(mockSlot, 1, 1);

        assertEquals(date1, date2);
        assertEquals(date1.getMonth(), date2.getMonth());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        MonthDate date1 = new MonthDate(mockSlot, 1, 1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        MonthDate date1 = new MonthDate(mockSlot, 1, 1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        MonthDate date1 = new MonthDate(mockSlot, 1, 1);
        MonthDate date2 = new MonthDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getMonth(), date2.getMonth());
        assertEquals(date1.getYear(), date2.getYear());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentYear() {
        MonthDate date1 = new MonthDate(mockSlot, 1, 1);
        MonthDate date2 = new MonthDate(mockSlot, 2, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getMonth(), date2.getMonth());
        assertNotEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentMonth() {
        MonthDate date1 = new MonthDate(mockSlot, 1, 1);
        MonthDate date2 = new MonthDate(mockSlot, 1, 2);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getMonth(), date2.getMonth());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
