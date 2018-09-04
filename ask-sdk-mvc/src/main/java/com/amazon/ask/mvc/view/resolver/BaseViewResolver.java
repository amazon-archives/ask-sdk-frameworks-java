package com.amazon.ask.mvc.view.resolver;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.mvc.plugin.ViewResolver;
import com.amazon.ask.mvc.view.ModelAndView;
import com.amazon.ask.mvc.view.View;
import com.amazon.ask.mvc.view.resolver.cache.SingleThreadedViewCache;
import com.amazon.ask.mvc.view.resolver.cache.ViewCache;
import com.amazon.ask.mvc.view.resolver.candidate.LocaleViewCandidateEnumerator;
import com.amazon.ask.mvc.view.resolver.candidate.ViewCandidateEnumerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Implements default view resolution logic:
 * - Enumerate and filter through an ordered set of candidates for the request
 * - (Optional) Complete partial view names by appending a prefix and suffix
 * - (Optional) Exclude views if they don't match regular expression
 * - Load view and cache it for re-use
 */
public abstract class BaseViewResolver implements ViewResolver {
    /** Object mapper for parsing {@link Response} json from views **/
    private static final ObjectMapper RESPONSE_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final ObjectMapper mapper;
    protected final ViewCache cache;
    protected final List<ViewCandidateEnumerator> viewCandidateEnumerators;
    protected final Collection<Pattern> viewNamePatterns;
    protected final String prefix;
    protected final String suffix;

    protected BaseViewResolver(ObjectMapper mapper, ViewCache cache, List<ViewCandidateEnumerator> viewCandidateEnumerators,
                               Collection<Pattern> viewNamePatterns, String prefix, String suffix) {
        this.mapper = assertNotNull(mapper, "mapper");
        this.cache = cache == null ? SingleThreadedViewCache.defaultCache() : cache;
        this.viewCandidateEnumerators = viewCandidateEnumerators == null ? Collections.singletonList(new LocaleViewCandidateEnumerator()) : viewCandidateEnumerators;
        this.viewNamePatterns = viewNamePatterns == null ? Collections.emptyList() : viewNamePatterns;
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
    }

    @Override
    public Optional<View> resolve(Object handlerOutput, RequestEnvelope requestEnvelope) throws Exception {
        if (handlerOutput instanceof ModelAndView) {
            ModelAndView modelAndView = (ModelAndView) handlerOutput;
            if (supports(modelAndView)) {
                return resolve(modelAndView, requestEnvelope);
            }
        }
        return Optional.empty();
    }

    protected Optional<View> resolve(ModelAndView modelAndView, RequestEnvelope requestEnvelope) throws Exception {
        for (ViewCandidateEnumerator enumerator: this.viewCandidateEnumerators) {
            Iterator<String> candidates = enumerator.enumerate(modelAndView.getViewName(), requestEnvelope).iterator();
            while (candidates.hasNext()) {
                String candidate = buildCompleteViewName(candidates.next());
                logger.trace("Attempting to resolve view candidate: {}", candidate);

                if (candidateExists(candidate)) {
                    View view = cache.getOrCreate(candidate, () -> resolveCandidate(candidate));
                    logger.trace("Resolved view candidate: {}", candidate);
                    return Optional.of(view);
                }
            }
        }
        return Optional.empty();
    }

    protected abstract boolean candidateExists(String candidate);

    /**
     * Resolve a view candidate.
     *
     * @param candidate view name prefixed and suffixed with
     * @return the view if it exists
     * @throws Exception if there was an error loading or parsing the view
     */
    protected abstract View resolveCandidate(String candidate) throws Exception;

    /**
     * Check if this view resolver supports this view
     */
    protected boolean supports(ModelAndView modelAndView) {
        return viewNamePatterns.isEmpty() || viewNamePatterns.stream().anyMatch(pattern -> pattern.matcher(modelAndView.getViewName()).matches());
    }

    /**
     * Builds a view value by concatenating the prefix, view name and suffix
     *
     * @param viewName partial view name
     * @return full view name
     */
    protected String buildCompleteViewName(String viewName) {
        return prefix + viewName + suffix;
    }

    /**
     * Generic builder to support polymorphic constructors of view resolvers.
     *
     * @param <Self> type of this builder
     */
    public static class Builder<Self extends Builder<Self>> {
        protected ObjectMapper mapper = RESPONSE_MAPPER;
        protected ViewCache cache;
        protected List<ViewCandidateEnumerator> viewCandidateEnumerators;
        protected Collection<Pattern> viewNamePatterns;
        protected String prefix;
        protected String suffix;

        public Self withObjectMapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return getThis();
        }

        public Self withCache(ViewCache cache) {
            this.cache = cache;
            return getThis();
        }

        public Self withViewCandidateEnumerator(List<ViewCandidateEnumerator> viewCandidateEnumerators) {
            this.viewCandidateEnumerators = viewCandidateEnumerators;
            return getThis();
        }

        public Self addViewCandidateEnumerator(ViewCandidateEnumerator viewCandidateEnumerator) {
            return addViewCandidateEnumerators(viewCandidateEnumerator);
        }

        public Self addViewCandidateEnumerators(ViewCandidateEnumerator... viewCandidateEnumerators) {
            return addViewCandidateEnumerator(Arrays.asList(viewCandidateEnumerators));
        }

        public Self addViewCandidateEnumerator(Collection<ViewCandidateEnumerator> viewCandidateEnumerators) {
            if (this.viewCandidateEnumerators == null) {
                this.viewCandidateEnumerators = new ArrayList<>();
            }
            this.viewCandidateEnumerators.addAll(viewCandidateEnumerators);
            return getThis();
        }

        public Self withViewNamePatterns(Collection<Pattern> viewNamePatterns) {
            this.viewNamePatterns = viewNamePatterns;
            return getThis();
        }

        public Self withPrefix(String prefix) {
            this.prefix = prefix;
            return getThis();
        }

        public Self withSuffix(String suffix) {
            this.suffix = suffix;
            return getThis();
        }

        @SuppressWarnings("unchecked")
        protected Self getThis() {
            return (Self) this;
        }
    }
}
