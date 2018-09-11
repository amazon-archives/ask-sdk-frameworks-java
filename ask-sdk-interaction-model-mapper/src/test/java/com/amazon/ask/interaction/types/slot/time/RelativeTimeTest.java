/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.types.slot.time;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RelativeTimeTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test
    public void testEqualsSelf() {
        RelativeTime test = new RelativeTime(mockSlot, TimeOfDay.MO);

        assertEquals(test, test);
        assertEquals(test.getTime(), test.getTime());
        assertEquals(test.getSlot(), test.getSlot());
        assertEquals(test.hashCode(), test.hashCode());
        assertEquals(test.toString(), test.toString());
    }

    @Test
    public void testEquals() {
        RelativeTime left = new RelativeTime(mockSlot, TimeOfDay.MO);
        RelativeTime right = new RelativeTime(mockSlot, TimeOfDay.MO);

        assertEquals(left, right);
        assertEquals(left.getTime(), right.getTime());
        assertEquals(left.getSlot(), right.getSlot());
        assertEquals(left.hashCode(), right.hashCode());
        assertEquals(left.toString(), right.toString());
    }

    @Test
    public void testNotEqualsNull() {
        RelativeTime test = new RelativeTime(mockSlot, TimeOfDay.MO);
        assertNotEquals(test, null);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        RelativeTime test = new RelativeTime(mockSlot, TimeOfDay.MO);
        assertNotEquals(test, "test");
        assertNotEquals("test", test);
    }

    @Test
    public void testNotEqualsDifferentTime() {
        RelativeTime left = new RelativeTime(mockSlot, TimeOfDay.MO);
        RelativeTime right = new RelativeTime(mockSlot, TimeOfDay.EV);

        assertNotEquals(left, right);
        assertNotEquals(left.getTime(), right.getTime());
        assertEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        RelativeTime left = new RelativeTime(mockSlot, TimeOfDay.MO);
        RelativeTime right = new RelativeTime(
            Slot.builder()
                .withName("test")
                .withValue("different")
                .build(),
            TimeOfDay.MO);

        assertNotEquals(left, right);
        assertEquals(left.getTime(), right.getTime());
        assertNotEquals(left.getSlot(), right.getSlot());
        assertNotEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left.toString(), right.toString());
    }
}
