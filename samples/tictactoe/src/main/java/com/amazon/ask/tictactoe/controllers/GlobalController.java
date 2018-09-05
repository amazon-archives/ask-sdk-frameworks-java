package com.amazon.ask.tictactoe.controllers;

import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.tictactoe.Constants;

/**
 *
 */
public class GlobalController {

    @IntentMapping(type = StopIntent.class)
    public Response stop() {
        return getEndResponse();
    }

    @IntentMapping(type = CancelIntent.class)
    public Response cancel() {
        return getEndResponse();
    }

    private Response getEndResponse() {
        return Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText(Constants.END_RESPONSE)
                .build())
            .build();
    }

    @RequestMapping(SessionEndedRequest.class)
    public void sessionEnded(SessionEndedRequest request) {
        System.out.println(String.format("Session ended with reason %s", request.getReason()));
    }
}
