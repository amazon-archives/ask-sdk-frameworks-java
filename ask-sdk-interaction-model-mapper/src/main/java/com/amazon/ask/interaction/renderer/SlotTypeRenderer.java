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
        List<SlotTypeValue> values = new ArrayList<>(slotData.getValues().size());
        for (Map.Entry<String, SlotValue> entry: slotData.getValues().entrySet()) {
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