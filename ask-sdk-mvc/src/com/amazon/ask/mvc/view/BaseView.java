package com.amazon.ask.mvc.view;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Appends the request envelope to the mode, renders the response to a JSON string and parses the result.
 */
public abstract class BaseView implements View {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final ObjectMapper mapper;

    protected BaseView(ObjectMapper mapper) {
        this.mapper = assertNotNull(mapper, "mapper");
    }

    @Override
    public Response render(Object mav, RequestEnvelope requestEnvelope) throws Exception {
        return render(prepareModel((ModelAndView) mav, requestEnvelope));
    }

    protected Response render(Map<String, Object> model) throws Exception {
        return mapper.readValue(renderInternal(model), Response.class);
    }

    protected abstract String renderInternal(Map<String, Object> model) throws Exception;

    protected Map<String, Object> prepareModel(ModelAndView mav, RequestEnvelope requestEnvelope) {
        Map<String, Object> model = new HashMap<>(mav.getModel());
        model.put("envelope", requestEnvelope);
        model.put("request", requestEnvelope.getRequest());
        model.put("session", requestEnvelope.getSession());
        return model;
    }

}
