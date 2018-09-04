package com.amazon.ask.mvc.view.resolver.candidate;

import com.amazon.ask.model.RequestEnvelope;

import java.util.stream.Stream;

/**
 * Enumerate through view candidates in order of preference for this particular request.
 *
 * @see LocaleViewCandidateEnumerator
 */
public interface ViewCandidateEnumerator {
    Stream<String> enumerate(String viewName, RequestEnvelope requestEnvelope);
}
