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

import static com.amazon.ask.interaction.Utils.checkArgument;

/**
 * Utterances that combined to a year (such as “next year”) convert to a date containing just the year: 2016.
 */
public class YearDate extends AmazonDate {
    private final int year;

    public YearDate(Slot slot, int year) {
        super(slot);
        checkArgument(year >= 0, "year must be >= 0");
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        YearDate yearDate = (YearDate) o;
        return year == yearDate.year;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), year);
    }

    @Override
    public String toString() {
        return "YearDate{" + "year=" + year + ", slot=" + getSlot() + '}';
    }
}