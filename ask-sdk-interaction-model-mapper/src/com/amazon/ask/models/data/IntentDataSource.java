package com.amazon.ask.models.data;

import com.amazon.ask.models.renderer.RenderContext;
import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.definition.IntentDefinition;

import java.util.function.Function;

/**
 *
 */
public interface IntentDataSource extends Function<RenderContext<IntentDefinition>, IntentData> {
}
