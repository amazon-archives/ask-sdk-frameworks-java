/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 */
public class AmazonNumberTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new AmazonNumber(null, 1);
    }

    @Test
    public void testNumber() {
        new AmazonNumber(mockSlot, 0);
        new AmazonNumber(mockSlot, 1);
        new AmazonNumber(mockSlot, -1);
    }

    @Test
    public void testEqualsSelf() {
        AmazonNumber date = new AmazonNumber(mockSlot, 1);

        assertEquals(date, date);
        assertEquals(date.getNumber(), date.getNumber());
        assertEquals(date.getSlot(), date.getSlot());
        assertEquals(date.hashCode(), date.hashCode());
        assertEquals(date.toString(), date.toString());
    }

    @Test
    public void testEquals() {
        AmazonNumber date1 = new AmazonNumber(mockSlot, 1);
        AmazonNumber date2 = new AmazonNumber(mockSlot, 1);

        assertEquals(date1, date2);
        assertEquals(date1.getNumber(), date2.getNumber());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertEquals(date1.hashCode(), date2.hashCode());
        assertEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        AmazonNumber date1 = new AmazonNumber(mockSlot, 1);

        assertNotEquals(date1, null);
        assertNotEquals(null, date1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        AmazonNumber date1 = new AmazonNumber(mockSlot, 1);

        assertNotEquals(date1, "different");
        assertNotEquals("different", date1);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        AmazonNumber date1 = new AmazonNumber(mockSlot, 1);
        AmazonNumber date2 = new AmazonNumber(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 1);

        assertNotEquals(date1, date2);
        assertEquals(date1.getNumber(), date2.getNumber());
        assertNotEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }

    @Test
    public void testNotEqualsDifferentNumber() {
        AmazonNumber date1 = new AmazonNumber(mockSlot, 1);
        AmazonNumber date2 = new AmazonNumber(mockSlot, 2);

        assertNotEquals(date1, date2);
        assertNotEquals(date1.getNumber(), date2.getNumber());
        assertEquals(date1.getSlot(), date2.getSlot());
        assertNotEquals(date1.hashCode(), date2.hashCode());
        assertNotEquals(date1.toString(), date2.toString());
    }
}
