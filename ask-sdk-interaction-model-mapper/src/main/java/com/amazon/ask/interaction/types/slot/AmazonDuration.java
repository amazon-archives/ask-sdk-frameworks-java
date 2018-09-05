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
import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonDurationParser;

import java.time.Duration;
import java.time.Period;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#duration">Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.DURATION")
@SlotPropertyReader(AmazonDurationParser.class)
public class AmazonDuration extends BaseSlotValue {
    private final Period period;
    private final Duration duration;

    public AmazonDuration(Slot slot, Period period, Duration duration) {
        this.setSlot(slot);
        this.period = assertNotNull(period, "period");
        this.duration = assertNotNull(duration, "duration");
    }

    public Period getPeriod() {
        return period;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AmazonDuration that = (AmazonDuration) o;
        return Objects.equals(period, that.period) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), period, duration);
    }

    @Override
    public String toString() {
        return "AmazonDuration{" + "period=" + period + ", duration=" + duration + ", slot=" + getSlot() + '}';
    }
}
