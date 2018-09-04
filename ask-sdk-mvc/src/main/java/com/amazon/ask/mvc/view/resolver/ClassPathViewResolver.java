package com.amazon.ask.mvc.view.resolver;

import com.amazon.ask.mvc.view.View;
import com.amazon.ask.mvc.view.resolver.cache.ViewCache;
import com.amazon.ask.mvc.view.resolver.candidate.ViewCandidateEnumerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Reads view candidates from a resource file.
 *
 * Resource class defaults to the resolver's class if none specified.
 */
public abstract class ClassPathViewResolver extends BaseViewResolver {
    protected final Class<?> resourceClass;

    protected ClassPathViewResolver(ObjectMapper mapper, ViewCache cache, List<ViewCandidateEnumerator> viewCandidateEnumerators,
                                    Collection<Pattern> viewNamePatterns, String prefix, String suffix, Class<?> resourceClass) {
        super(mapper, cache, viewCandidateEnumerators, viewNamePatterns, prefix, suffix);
        this.resourceClass  = resourceClass == null ? getClass() : resourceClass;
    }

    @Override
    protected boolean candidateExists(String candidate) {
        return resourceClass.getResource(candidate) != null;
    }

    @Override
    protected View resolveCandidate(String viewName) throws Exception {
        return loadView(viewName, resourceClass.getResource(viewName));
    }

    protected abstract View loadView(String viewName, URL viewResource) throws Exception;

    public static class Builder<Self extends Builder<Self>> extends BaseViewResolver.Builder<Self> {
        protected Class<?> resourceClass;

        /**
         * @param resourceClass the class to load resource files from
         * @return this
         */
        public Self withResourceClass(Class<?> resourceClass) {
            this.resourceClass = resourceClass;
            return getThis();
        }
    }
}
