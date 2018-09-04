package com.amazon.ask.decisiontree.handlers.exception;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.exception.UnhandledSkillException;
import com.amazon.ask.model.Response;
import java.util.Optional;

public class UnhandledSkillExceptionHandler implements ExceptionHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, Throwable throwable) {
        return throwable instanceof UnhandledSkillException;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, Throwable throwable) {
        return handlerInput.getResponseBuilder()
            .withSpeech("Sorry I couldn't understand that. Please try again")
            .withReprompt("Please try again")
            .withShouldEndSession(false)
            .build();
    }
}
