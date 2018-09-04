package com.amazon.ask.interaction.data;

import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.renderer.RenderContext;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 */
public interface IntentDataResolver extends Function<RenderContext<IntentDefinition>, Stream<IntentData>> {
}
