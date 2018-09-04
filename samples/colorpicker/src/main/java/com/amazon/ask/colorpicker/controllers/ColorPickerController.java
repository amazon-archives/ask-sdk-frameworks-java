package com.amazon.ask.colorpicker.controllers;

import com.amazon.ask.colorpicker.intents.MyColorIsIntent;
import com.amazon.ask.colorpicker.intents.WhatsMyColorIntent;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.view.ModelAndView;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * Re-implementation of https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/samples/colorpicker
 * using MVC and Models framework.
 */
public class ColorPickerController {
    private static final String COLOR_SESSION_KEY = "COLOR";
    private static final String WELCOME_MESSAGE = "Welcome to the Alexa Skills Kit sample. Please tell me your favorite color by saying, my favorite color is red";
    private static final String REPROMPT_MESSAGE = "Please tell me your favorite color by saying, my favorite color is red";

    @RequestMapping(types = LaunchRequest.class)
    public ModelAndView launch() {
        return new ModelAndView("start", Collections.singletonMap("isWelcomeMessage", true));
    }

    @RequestMapping(types = SessionEndedRequest.class)
    public Response sessionEnd(ResponseBuilder responseBuilder) {
        // any cleanup logic goes here
        return responseBuilder.build().get();
    }

    @IntentMapping(type = HelpIntent.class)
    public Response handleHelp(ResponseBuilder responseBuilder) {
        // Example of rendering a response using the default input response builder
        return responseBuilder
            .withSpeech("You can tell me your favorite color by saying, my favorite color is red")
            .withReprompt(REPROMPT_MESSAGE)
            .build()
            .get();
    }

    // Remaining handlers make use of Freemarker ModelAndView
    @IntentMapping(type = StopIntent.class)
    public ModelAndView handleStop() {
        return new ModelAndView("stop");
    }

    @IntentMapping(type = CancelIntent.class)
    public ModelAndView handleCancel() {
        return new ModelAndView("stop");
    }

    @IntentMapping(type = MyColorIsIntent.class)
    public ModelAndView handleMyColorIs(MyColorIsIntent intent, AttributesManager attributesManager) {
        ModelAndView view = new ModelAndView("my_color_is");
        view.put("isAskResponse", intent.getColor() == null);
        if (intent.getColor() != null) {
            Map<String, Object> sessionAttributes = Collections.singletonMap(COLOR_SESSION_KEY, intent.getColor().getSlot().getValue());
            attributesManager.setSessionAttributes(sessionAttributes);

            view.put("favoriteColor", intent.getColor().getSlot().getValue());
        }

        return view;
    }

    @IntentMapping(type = WhatsMyColorIntent.class)
    public ModelAndView handleWhatIsMyColor(Session session) {
        if (session.getAttributes() == null) {
            return new ModelAndView("start", Collections.singletonMap("isWelcomeMessage", false));
        }

        ModelAndView view = new ModelAndView("what_is_my_color");
        String favoriteColor = (String) session.getAttributes().get(COLOR_SESSION_KEY);
        if (favoriteColor != null && !favoriteColor.isEmpty()) {
            view.put("favoriteColor", favoriteColor);
        }
        return view;
    }
}
