/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.renderer;


import com.amazon.ask.interaction.model.SlotTypeValue;
import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.data.model.SlotTypeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders the slot type interaction model
 */
public class SlotTypeRenderer {
    /**
     * Render the slot type interaction model
     *
     * @param slotType slot type definition
     * @param slotData slot type data
     * @return custom slot type interaction model
     */
    public com.amazon.ask.interaction.model.SlotType renderSlotType(SlotTypeDefinition slotType, SlotTypeData slotData) {
        assertNotNull(slotType, "slotType");
        assertNotNull(slotData, "slotMetadata");

        return com.amazon.ask.interaction.model.SlotType.builder()
            .withName(slotType.getName())
            .withValues(renderValues(slotData))
            .build();
    }

    protected List<SlotTypeValue> renderValues(SlotTypeData slotData) {
        List<SlotTypeValue> values = new ArrayList<>(slotData.getValuesIndex().size());
        for (Map.Entry<String, SlotValue> entry: slotData.getValuesIndex().entrySet()) {
            values.add(SlotTypeValue.builder()
                .withId(entry.getKey())
                .withName(SlotValue.builder()
                    .withValue(entry.getValue().getValue())
                    .withSynonyms(entry.getValue().getSynonyms())
                    .build())
                .build());
        }
        return values;
    }
}
