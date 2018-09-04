package com.amazon.ask.interaction.data.source;

import com.amazon.ask.interaction.renderer.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.amazon.ask.util.ValidationUtils.assertNotEmpty;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Loads metadata from a resource file.
 *
 * Looks for files on the classpath by enumerating a set of {@link ResourceCandidateEnumerator}
 */
public class ResourceSource<T, D> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceSource.class);

    private final Class resourceClass;
    private final Codec<D> codec;
    private final String name;
    private final String suffix;
    private final Set<ResourceCandidateEnumerator> resourceCandidateEnumerators;

    /**
     * Create a resource supplier using the default {@link JsonCodec}
     *
     * @param resourceClass class from which the (relative or absolute) path resolves the resource file
     * @param codec to apply file add
     * @param name of resource
     * @param suffix of resource
     * @param resourceCandidateEnumerators non-empty set of resource enumerators/scanners
     * @see Class#getResource(String)
     */
    public ResourceSource(Class resourceClass, Codec<D> codec, String name, String suffix, Set<ResourceCandidateEnumerator> resourceCandidateEnumerators) {
        this.resourceClass = assertNotNull(resourceClass, "resourceClass");
        this.name = assertNotNull(name, "name");
        this.suffix = suffix == null ? ".json" : suffix;
        this.codec = assertNotNull(codec, "codec");
        this.resourceCandidateEnumerators = Collections.unmodifiableSet(assertNotEmpty(resourceCandidateEnumerators, "resourceCandidateEnumerators"));
    }

    public D apply(RenderContext<T> renderContext) {
        return this.resourceCandidateEnumerators.stream()
            .flatMap(enumerator -> enumerator
                .enumerate(name, renderContext)
                .map(candidate -> candidate + suffix)
                .map(resourceClass::getResource)
                .filter(Objects::nonNull))
            .findFirst()
            .map(url -> {
                try (InputStream stream = url.openStream()) {
                    return codec.read(stream);
                } catch (IOException e) {
                    LOGGER.error("Failed to read URL: " + url, e);
                    throw new RuntimeException("Failed to read URL: " + url, e);
                }
            })
            .orElseThrow(() -> new ResourceNotFoundException("No resources found"));
    }

    public static abstract class Builder<Self extends Builder<Self, T, D>, T, D> {
        protected Class resourceClass;
        protected Codec<D> codec;
        protected String name;
        protected String suffix;
        protected Set<ResourceCandidateEnumerator> resourceCandidateEnumerators;

        public Builder() {
            this.addResourceCandidateEnumerator(LocaleResourceCandidateEnumerator.getInstance());
        }

        public Self withResourceCandidateEnumerators(Set<ResourceCandidateEnumerator> resourceCandidateEnumerators) {
            this.resourceCandidateEnumerators = resourceCandidateEnumerators;
            return getThis();
        }

        public Self addResourceCandidateEnumerators(Set<ResourceCandidateEnumerator> resourceCandidateEnumerators) {
            resourceCandidateEnumerators.forEach(this::addResourceCandidateEnumerator);
            return getThis();
        }

        public Self addResourceCandidateEnumerator(ResourceCandidateEnumerator resourceCandidateEnumerator) {
            if (this.resourceCandidateEnumerators == null) {
                this.resourceCandidateEnumerators = new LinkedHashSet<>();
            }
            this.resourceCandidateEnumerators.add(resourceCandidateEnumerator);
            return getThis();
        }

        public Self withResourceClass(Class resourceClass) {
            this.resourceClass = resourceClass;
            return getThis();
        }

        public Self withCodec(Codec<D> codec) {
            this.codec = codec;
            return getThis();
        }

        public Self withName(String name) {
            this.name = name;
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
