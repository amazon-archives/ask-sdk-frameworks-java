package com.amazon.ask.models.data;

import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.definition.IntentDefinition;
import com.amazon.ask.models.renderer.RenderContext;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 */
public interface IntentDataResolver extends Function<RenderContext<IntentDefinition>, Stream<IntentData>> {
}
