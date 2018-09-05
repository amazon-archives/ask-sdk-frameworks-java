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
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.interaction.mapper.SlotValueParseException;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Parses a Custom Slot symbol to its corresponding Enum value.
 *
 * https://developer.amazon.com/docs/custom-skills/define-synonyms-and-ids-for-slot-type-values-entity-resolution.html
 */
public class EnumCustomSlotReader<T extends Enum<T>> implements SlotPropertyReader<T> {
    private final Class<T> slotClass;

    public EnumCustomSlotReader(Class<T> slotClass) {
        this.slotClass = assertNotNull(slotClass, "slotClass");
    }

    @Override
    public T read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        try {
            if (slot.getResolutions() != null &&
                slot.getResolutions().getResolutionsPerAuthority() != null &&
                !slot.getResolutions().getResolutionsPerAuthority().isEmpty()) {

                // TODO: Unit test this
                for (Resolution resolution : slot.getResolutions().getResolutionsPerAuthority()) {
                    if (resolution.getStatus().getCode() == StatusCode.ER_SUCCESS_MATCH) {
                        return Enum.valueOf(slotClass, resolution.getValues().get(0).getValue().getId());
                    }
                }
            }
            return Enum.valueOf(slotClass, slot.getValue());
        } catch (IllegalArgumentException ex) {
            throw new SlotValueParseException(slot, slotClass, ex);
        }
    }
}
