package com.amazon.ask.mvc.view.resolver;

import com.amazon.ask.mvc.view.BaseView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders a response using a {@link ScriptEngine}
 */
public class ScriptEngineView extends BaseView {
    protected final ScriptEngine scriptEngine;
    protected final String script;
    protected final String renderFunction;
    protected final String renderObject;

    public ScriptEngineView(ScriptEngine scriptEngine, String script, String renderObject, String renderFunction, ObjectMapper mapper) throws Exception {
        super(mapper);
        this.scriptEngine = assertNotNull(scriptEngine, "scriptEngine");
        this.script = assertNotNull(script, "script");
        this.renderObject = renderObject;
        this.renderFunction = renderFunction;

        if (renderFunction != null) {
            // render logic is attached to a global function or object
            // script needs to be eval'ed once so its functions and objects are available in the engine state for invocation
            if (scriptEngine instanceof Invocable) {
                this.scriptEngine.eval(script);
            } else {
                throw new IllegalArgumentException("ScriptEngine must be an Invocable if renderFunction or renderObject are specified");
            }
        }
    }

    @Override
    protected String renderInternal(Map<String, Object> model) throws Exception {
        SimpleBindings bindings = new SimpleBindings(model);
        Object result;
        if (renderFunction == null && renderObject == null) {
            // result of script is the response, no method is invoked and the model attributes are bounded globally
            result = scriptEngine.eval(script, bindings);
        } else{
            if (renderObject != null) {
                Object thiz = scriptEngine.eval(renderObject);
                result = ((Invocable) scriptEngine).invokeMethod(thiz, renderFunction, bindings);
            } else {
                result = ((Invocable) scriptEngine).invokeFunction(renderFunction, bindings);
            }
        }

        return toJson(result);
    }

    /**
     * By default we assume the result is a json string, but we may want to override that.
     */
    protected String toJson(Object result) throws Exception {
        return (String) result;
    }
}
