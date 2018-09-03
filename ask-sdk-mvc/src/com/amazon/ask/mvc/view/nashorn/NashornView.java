package com.amazon.ask.mvc.view.nashorn;

import com.amazon.ask.mvc.view.resolver.ScriptEngineView;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Renders a response by passing the model to a JavaScript script. There are three distinct ways to
 * write a view script:
 *
 * 1) A globally scoped function:
 * <code>
 *     function render(model) {
 *         return {
 *             type: 'PlainText',
 *             text: 'Hello ' + model.name
 *         };
 *     }
 * </code>
 * 2) A globally scoped object's local function:
 * <code>
 *     var renderer = {
 *         render: function(model) {
 *             return {
 *                 type: 'PlainText',
 *                 text: 'Hello ' + model.name
 *             };
 *         }
 *     };
 * </code>
 * 3) Model attributes bound to the global scope and the result is the final expression:
 * <code>
 *     var result = {
 *         type: 'PlainText',
 *         text: 'Hello ' + name
 *     }
 *
 *     result; // final expression is the result
 * </code>
 */
public class NashornView extends ScriptEngineView {
    public NashornView(ScriptEngine scriptEngine, String script, String renderObject, String renderFunction, ObjectMapper mapper) throws Exception {
        super(scriptEngine, script, renderObject, renderFunction, mapper);
    }

    /**
     * Support returning a JS object instead of JSON string.
     */
    @Override
    protected String toJson(Object result) throws ScriptException {
        ScriptObjectMirror json = (ScriptObjectMirror) scriptEngine.eval("JSON");
        return (String) json.callMember("stringify", result);
    }
}
