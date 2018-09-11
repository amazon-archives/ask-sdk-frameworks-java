/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view.resolver;

import com.amazon.ask.mvc.view.View;
import com.amazon.ask.mvc.view.resolver.cache.ViewCache;
import com.amazon.ask.mvc.view.resolver.candidate.ViewCandidateEnumerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Base implementation for view which invokes javax scripts to render responses.
 */
public abstract class ScriptEngineViewResolver extends ClassPathViewResolver {
    protected final ScriptEngineManager scriptEngineManager;
    protected final String scriptEngineName;
    protected final String renderObject;
    protected final String renderFunction;

    protected ScriptEngineViewResolver(ObjectMapper mapper, ViewCache cache, List<ViewCandidateEnumerator> viewCandidateEnumerators,
                                       Collection<Pattern> viewNamePatterns, String prefix, String suffix, Class<?> resourceClass,
                                       ScriptEngineManager scriptEngineManager, String scriptEngineName, String renderObject, String renderFunction) {
        super(mapper, cache, viewCandidateEnumerators, viewNamePatterns, prefix, suffix, resourceClass);
        this.scriptEngineManager = scriptEngineManager == null ? new ScriptEngineManager(resourceClass.getClassLoader()) : scriptEngineManager;
        this.scriptEngineName = assertNotNull(scriptEngineName, "scriptEngineName");
        if (renderObject != null && renderFunction == null) {
            throw new IllegalArgumentException("renderFunction must be non-null if renderObject is set");
        }
        this.renderObject = renderObject;
        this.renderFunction = renderFunction;
    }

    @Override
    protected View loadView(String viewName, URL viewResource) throws Exception {
        try (Reader scriptReader = new BufferedReader(new InputStreamReader(viewResource.openStream()))) {
            return createView(getScriptEngine(), scriptReader);
        } catch (Exception ex) {
            logger.error("Failed to read ScriptEngine View", ex);
            throw ex;
        }
    }

    protected ScriptEngine getScriptEngine() {
        return scriptEngineManager.getEngineByName(scriptEngineName);
    }

    /**
     * Create a view for a script.
     *
     * @param scriptEngine the script's engine
     * @param scriptReader the script content reader
     * @return the view, encapsulating the script
     * @throws Exception if the script could not be read or compiled
     */
    protected abstract View createView(ScriptEngine scriptEngine, Reader scriptReader) throws Exception;

    public static class Builder<Self extends Builder<Self>> extends ClassPathViewResolver.Builder<Self> {
        protected ScriptEngineManager scriptEngineManager;
        protected String scriptEngineName;
        protected String renderObject;
        protected String renderFunction;

        public Self withScriptEngineManager(ScriptEngineManager scriptEngineManager) {
            this.scriptEngineManager = scriptEngineManager;
            return getThis();
        }

        public Self withScriptEngineName(String scriptEngineName) {
            this.scriptEngineName = scriptEngineName;
            return getThis();
        }

        public Self withRenderObject(String renderObject, String renderFunction) {
            this.renderObject = renderObject;
            return withRenderFunction(renderFunction);
        }

        public Self withRenderFunction(String renderFunction) {
            this.renderFunction = renderFunction;
            return getThis();
        }
    }
}
