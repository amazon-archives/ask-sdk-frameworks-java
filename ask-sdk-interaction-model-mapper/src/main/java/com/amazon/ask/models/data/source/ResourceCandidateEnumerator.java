package com.amazon.ask.models.data.source;

import com.amazon.ask.models.renderer.RenderContext;

import java.util.stream.Stream;

/**
 *
 */
public interface ResourceCandidateEnumerator {
    Stream<String> enumerate(String name, RenderContext<?> renderContext);
}
