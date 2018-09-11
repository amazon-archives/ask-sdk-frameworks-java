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
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Utterances that combined to a season (such as “next winter”) convert to a date add the year and a season indicator:
 *
 * winter: WI, spring: SP, summer: SU, fall: FA)
 */
public class SeasonDate extends AmazonDate {
    private final int year;
    private final Season season;

    public SeasonDate(Slot slot, int year, Season season) {
        super(slot);
        checkArgument(year >= 0, "year must be >= 0");
        this.year = year;
        this.season = assertNotNull(season, "season");
    }

    public int getYear() {
        return this.year;
    }

    public Season getSeason() {
        return this.season;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        SeasonDate that = (SeasonDate) o;
        return year == that.year && season == that.season;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), year, season);
    }

    @Override
    public String toString() {
        return "SeasonDate{" + "year=" + year + ", season=" + season + ", slot=" + getSlot() + '}';
    }
}
