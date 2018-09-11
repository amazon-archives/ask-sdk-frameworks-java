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

import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonNumberParser;

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#number">Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.NUMBER")
@SlotPropertyReader(AmazonNumberParser.class)
public class AmazonNumber extends BaseSlotValue {
    private final long number;

    public AmazonNumber(com.amazon.ask.model.Slot slot, long number) {
        this.setSlot(assertNotNull(slot, "slot"));
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AmazonNumber that = (AmazonNumber) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number);
    }

    @Override
    public String toString() {
        return "AmazonNumber{" +
            "number=" + number +
            ", slot=" + getSlot() +
            '}';
    }
}
