package com.amazon.ask.models.data;

import com.amazon.ask.models.data.model.SlotTypeData;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.amazon.ask.models.renderer.RenderContext;

import java.util.function.Function;

/**
 *
 */
public interface SlotTypeDataSource extends Function<RenderContext<SlotTypeDefinition>, SlotTypeData> {
}
