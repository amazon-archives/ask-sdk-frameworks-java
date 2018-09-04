package com.amazon.ask.tictactoe.controllers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.models.types.intent.YesIntent;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestInterceptor;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.annotation.mapping.ResponseInterceptor;
import com.amazon.ask.mvc.mapper.Priority;
import com.amazon.ask.tictactoe.game.GameService;
import com.amazon.ask.tictactoe.game.GameState;
import com.amazon.ask.tictactoe.interaction.NewGame;

import static com.amazon.ask.mvc.mapper.Priority.MINIMUM;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Controller for when the skill is in no state.
 */
@WhenSessionAttribute(path = "state", matchNull = true)
public class MenuController {
    private final GameService gameService;

    public MenuController(GameService gameService) {
        this.gameService = assertNotNull(gameService, "gameService");
    }

    @RequestMapping(LaunchRequest.class)
    public Response onLaunch(AttributesManager attributesManager) {
        attributesManager.getSessionAttributes().put("confirm", "new-game");

        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("welcome to tic tac toe. do you want to play a game?")
                .build())
            .build();
    }

    @IntentMapping(type = YesIntent.class)
    @WhenSessionAttribute(path = "confirm", hasValues = "new-game")
    public Response confirmPlay(AttributesManager attributes) {
        attributes.getSessionAttributes().remove("confirm");
        return startNewGame(attributes);
    }

    @IntentMapping(type = NewGame.class)
    public Response onNewGame(AttributesManager attributes) {
        return startNewGame(attributes);
    }

    private Response startNewGame(AttributesManager attributes) {
        GameState state = gameService.newGame();
        gameService.saveGame(attributes, state);
        attributes.getSessionAttributes().put("state", "playing");

        return Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("player X to start")
                .build())
            .withShouldEndSession(false)
            .build();
    }

    @Priority(MINIMUM) // this method accepts all IntentRequests, so always consider it last
    @RequestMapping(types = IntentRequest.class)
    public Response unhandledIntent() {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("Do you want to play a game?")
                .build())
            .build();
    }

    @RequestInterceptor
    public void beforeMenuRequest(RequestEnvelope request) {
        System.out.println("menu request: " + request.getRequest().getRequestId());
    }

    @ResponseInterceptor
    public void afterMenuRequest(RequestEnvelope request) {
        System.out.println("menu response: " + request.getRequest().getRequestId());
    }
}
