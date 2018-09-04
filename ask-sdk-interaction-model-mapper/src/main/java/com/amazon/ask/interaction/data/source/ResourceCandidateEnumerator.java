package com.amazon.ask.interaction.data.source;

import com.amazon.ask.interaction.renderer.RenderContext;

import java.util.stream.Stream;

/**
 *
 */
public interface ResourceCandidateEnumerator {
    Stream<String> enumerate(String name, RenderContext<?> renderContext);
}
