package com.amazon.ask.interaction.data;

import com.amazon.ask.interaction.renderer.RenderContext;
import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.definition.IntentDefinition;

import java.util.function.Function;

/**
 *
 */
public interface IntentDataSource extends Function<RenderContext<IntentDefinition>, IntentData> {
}
