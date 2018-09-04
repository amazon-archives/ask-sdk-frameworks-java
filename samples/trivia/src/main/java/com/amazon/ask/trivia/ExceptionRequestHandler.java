package com.amazon.ask.trivia;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import java.util.Optional;

public class ExceptionRequestHandler implements ExceptionHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, Throwable throwable) {
        return true; //catch all
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, Throwable throwable) {
        System.out.println("Exception: " + throwable.toString());
        System.out.println("Exception thrown while receiving: " + handlerInput.getRequestEnvelope().getRequest().getType());
        return handlerInput.getResponseBuilder()
            .withSpeech("Sorry. I have problems answering your request. Please try again")
            .withShouldEndSession(true)
            .build();
    }
}
