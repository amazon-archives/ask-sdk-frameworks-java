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

import java.time.LocalTime;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Values that represent a specific time are provided to your skill in ISO-8601 time format. Midnight is
 * represented as 00:00
 */
public class AbsoluteTime extends AmazonTime {
    private final LocalTime time;

    public AbsoluteTime(Slot slot, LocalTime time) {
        super(slot);
        this.time = assertNotNull(time, "time");
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AbsoluteTime that = (AbsoluteTime) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), time);
    }

    @Override
    public String toString() {
        return "AbsoluteTime{" +
            "time=" + time +
            "slot=" + getSlot() +
            '}';
    }
}
