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

import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.AmazonDateParser;
import com.amazon.ask.interaction.types.slot.BaseSlotValue;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Converts words that represent dates into a date format.
 *
 * @see PresentRef
 * @see DecadeDate
 * @see MonthDate
 * @see SeasonDate
 * @see SpecificDate
 * @see WeekDate
 * @see WeekendDate
 * @see YearDate
 * @see AmazonDateParser
 *
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#date">Slot Type Reference</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.DATE")
@SlotPropertyReader(AmazonDateParser.class)
public abstract class AmazonDate extends BaseSlotValue {
    AmazonDate(com.amazon.ask.model.Slot slot) {
        this.setSlot(assertNotNull(slot, "slot"));
    }
}
