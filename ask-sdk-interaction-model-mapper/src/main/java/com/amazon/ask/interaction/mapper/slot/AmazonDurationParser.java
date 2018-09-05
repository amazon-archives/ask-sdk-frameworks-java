/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.SlotValueParseException;
import com.amazon.ask.interaction.types.slot.AmazonDuration;

import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 *
 */
public class AmazonDurationParser implements SlotPropertyReader<AmazonDuration> {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("T");

    @Override
    public AmazonDuration read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        String value = slot.getValue();
        String[] split = SPLIT_PATTERN.split(value, 2);
        try {
            if (split.length == 1) {
                return new AmazonDuration(slot, Period.parse(value), Duration.ZERO);
            } else {
                Period p;
                if (split[0].equals("P")) p = Period.ZERO;
                else p = Period.parse(split[0]);

                return new AmazonDuration(slot, p, Duration.parse("PT" + split[1]));
            }
        } catch (DateTimeParseException ex) {
            throw new SlotValueParseException(slot, AmazonDuration.class, ex);
        }
    }
}
