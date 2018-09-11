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
import com.amazon.ask.interaction.types.slot.FourDigitNumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class FourDigitNumberParser implements SlotPropertyReader<FourDigitNumber> {
    private static final Pattern REGEX = Pattern.compile("^([0-9])([0-9])([0-9])([0-9])$");

    @Override
    public FourDigitNumber read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        Matcher matcher = REGEX.matcher(slot.getValue());
        if (matcher.matches()) {
            return new FourDigitNumber(
                slot,
                Integer.valueOf(matcher.group(1)),
                Integer.valueOf(matcher.group(2)),
                Integer.valueOf(matcher.group(3)),
                Integer.valueOf(matcher.group(4))
            );
        } else {
            throw new SlotValueParseException(slot, FourDigitNumber.class);
        }
    }
}
