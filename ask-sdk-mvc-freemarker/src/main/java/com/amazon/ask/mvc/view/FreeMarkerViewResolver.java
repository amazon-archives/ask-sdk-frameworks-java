/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view;

import com.amazon.ask.mvc.view.resolver.ClassPathViewResolver;
import com.amazon.ask.mvc.view.resolver.cache.ViewCache;
import com.amazon.ask.mvc.view.resolver.candidate.ViewCandidateEnumerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Renders templates using the FreeMarker template engine. If {@link Configuration} is not specified, a default one will
 * be built with the following settings:
 * <ul>
 * <li>Templates loading from the class the classpath with the prefix "/views"</li>
 * <li>UTF-8 encoding</li>
 * </ul>
 * <p>
 * If a prefix is specified, view names will be resolved relative to that prefix. For example
 * If if the prefix is set to "/views/" and the suffix to ".ftl" and ModelAndView is used with a view value of "foo/my-template", there must be a template at
 * "/views/foo/my-template.ftl" in the classpath, or one for the locale of the request like
 * "/views/foo/my-template_en_US.ftl".
 */
public class FreeMarkerViewResolver extends ClassPathViewResolver {
    protected final Configuration configuration;

    protected FreeMarkerViewResolver(ObjectMapper mapper, ViewCache cache, List<ViewCandidateEnumerator> viewCandidateEnumerators,
                                     Collection<Pattern> viewNamePatterns, String prefix, String suffix, Class<?> resourceClass,
                                     Configuration configuration) {
        super(mapper, cache, viewCandidateEnumerators, viewNamePatterns, prefix, suffix, resourceClass);
        this.configuration = buildDefaultConfig(configuration, resourceClass);
    }

    /**
     * Builds a default config for FreeMarker which reads templates from the classpath under the path specified in the prefix
     * a prefix is specified.
     *
     * @param configuration free marker config
     * @param resourceClass class to load resources relative to
     * @return supplied configuration if not nul, otherwise a default one
     */
    protected Configuration buildDefaultConfig(Configuration configuration, Class<?> resourceClass) {
        if (configuration != null) {
            return configuration;
        }
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassForTemplateLoading(resourceClass == null ? getClass() : resourceClass, "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setCacheStorage(NullCacheStorage.INSTANCE);
        return cfg;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected View loadView(String viewName, URL viewResource) throws Exception {
        return new FreeMarkerView(mapper, configuration.getTemplate(viewName));
    }

    public static final class Builder extends ClassPathViewResolver.Builder<Builder> {
        protected Configuration configuration;

        public Builder() {
            this.suffix = ".ftl";
        }

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public FreeMarkerViewResolver build() {
            return new FreeMarkerViewResolver(mapper, cache, viewCandidateEnumerators, viewNamePatterns, prefix, suffix, resourceClass, configuration);
        }
    }
}
