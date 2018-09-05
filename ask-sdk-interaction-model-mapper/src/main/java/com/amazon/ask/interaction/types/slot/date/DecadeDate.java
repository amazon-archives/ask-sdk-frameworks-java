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

import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertIsPositive;

/**
 * Utterances that combined to a decade convert to a date indicating the decade: 201X.
 */
public class DecadeDate extends AmazonDate {
    private final int century;
    private final int decade;

    public DecadeDate(Slot slot, int century, int decade) {
        super(slot);
        this.century = assertIsPositive(century, "century");
        if (decade < 0) {
            throw new IllegalArgumentException("decade must be zero or positive");
        }
        this.decade = decade;
    }

    public int getCentury() {
        return century;
    }

    public int getDecade() {
        return decade;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        DecadeDate that = (DecadeDate) o;
        return century == that.century && decade == that.decade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), century, decade);
    }

    @Override
    public String toString() {
        return "DecadeDate{" + "century=" + century + ", decade=" + decade + ", slot=" + getSlot() + '}';
    }
}
