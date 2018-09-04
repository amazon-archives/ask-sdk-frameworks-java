package com.amazon.ask.interaction.data;

import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.renderer.RenderContext;

import java.util.function.Function;

/**
 *
 */
public interface SlotTypeDataSource extends Function<RenderContext<SlotTypeDefinition>, SlotTypeData> {
}
