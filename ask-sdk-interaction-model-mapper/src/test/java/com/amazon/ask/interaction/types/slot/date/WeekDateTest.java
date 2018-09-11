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

public class WeekDateTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new WeekDate(null, 1, 1);
    }

    @Test
    public void testWeek() {
        new WeekDate(mockSlot, 0, 0);
        new WeekDate(mockSlot, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeek() {
        new WeekDate(mockSlot, 1, -1);
    }

    @Test
    public void testYear() {
        new WeekDate(mockSlot, 0, 1);
        new WeekDate(mockSlot, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeYear() {
        new WeekDate(mockSlot, -1, 1);
    }

    @Test
    public void testEqualsSelf() {
        WeekDate date = new WeekDate(mockSlot, 1, 1);

        assertEquals(date, date);
        assertEquals(date.getWeek(), date.getWeek());
        assertEquals(date.getYear(), date.getYear());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        WeekDate date1 = new WeekDate(mockSlot, 1, 1);
        WeekDate date2 = new WeekDate(mockSlot, 1, 1);

        assertEquals(date1, date2);
        assertEquals(date1.getWeek(), date2.getWeek());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        WeekDate date1 = new WeekDate(mockSlot, 1, 1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        WeekDate date1 = new WeekDate(mockSlot, 1, 1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        WeekDate date1 = new WeekDate(mockSlot, 1, 1);
        WeekDate date2 = new WeekDate(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getWeek(), date2.getWeek());
        assertEquals(date1.getYear(), date2.getYear());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentYear() {
        WeekDate date1 = new WeekDate(mockSlot, 1, 1);
        WeekDate date2 = new WeekDate(mockSlot, 2, 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getWeek(), date2.getWeek());
        assertNotEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentWeek() {
        WeekDate date1 = new WeekDate(mockSlot, 1, 1);
        WeekDate date2 = new WeekDate(mockSlot, 1, 2);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getWeek(), date2.getWeek());
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
