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

import com.amazon.ask.util.ValidationUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Holds a view name used to render the response for the current request,
 * as well as the model used to render it.
 */
public class ModelAndView {
    protected final String viewName;
    protected final Map<String, Object> model;

    /**
     * Builds a new model and view with an empty model and the specified view name
     *
     * @param viewName name of the view
     */
    public ModelAndView(String viewName) {
        this(viewName, new HashMap<>());
    }

    /**
     * Builds a new model and view with the specified view name and a model
     *
     * @param viewName name of the view
     * @param model of key value pairs
     */
    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = ValidationUtils.assertStringNotEmpty(viewName, "viewName");
        this.model = new HashMap<>(model);
    }

    public int size() {
        return model.size();
    }

    public boolean isEmpty() {
        return model.isEmpty();
    }

    public boolean containsKey(Object key) {
        return model.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return model.containsValue(value);
    }

    public Object get(Object key) {
        return model.get(key);
    }

    public Object put(String key, Object value) {
        return model.put(key, value);
    }

    public Object remove(Object key) {
        return model.remove(key);
    }

    public void putAll(Map<? extends String, ?> m) {
        model.putAll(m);
    }

    public void clear() {
        model.clear();
    }

    public Set<String> keySet() {
        return model.keySet();
    }

    public Collection<Object> values() {
        return model.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return model.entrySet();
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        return model.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        model.forEach(action);
    }

    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        model.replaceAll(function);
    }

    public Object putIfAbsent(String key, Object value) {
        return model.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return model.remove(key, value);
    }

    public boolean replace(String key, Object oldValue, Object newValue) {
        return model.replace(key, oldValue, newValue);
    }

    public Object replace(String key, Object value) {
        return model.replace(key, value);
    }

    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return model.computeIfAbsent(key, mappingFunction);
    }

    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return model.computeIfPresent(key, remappingFunction);
    }

    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return model.compute(key, remappingFunction);
    }

    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return model.merge(key, value, remappingFunction);
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
