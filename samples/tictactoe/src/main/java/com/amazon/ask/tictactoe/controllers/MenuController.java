/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.tictactoe.controllers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.NoIntent;
import com.amazon.ask.interaction.types.intent.YesIntent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestInterceptor;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.annotation.mapping.ResponseInterceptor;
import com.amazon.ask.mvc.mapper.Priority;
import com.amazon.ask.tictactoe.Constants;
import com.amazon.ask.tictactoe.intents.NewGame;
import com.amazon.ask.tictactoe.service.GameService;
import com.amazon.ask.tictactoe.service.GameState;

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
                .withText("Welcome to Tic Tac Toe. You can play this game in a three by three square grid with your friend. " +
                    "You will be player X and your friend will be player O. You can say play a new game to start playing.")
                .build())
            .build();
    }

    @IntentMapping(type = HelpIntent.class)
    public Response help() {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText(Constants.MENU_HELP_RESPONSE)
                .build())
            .build();
    }

    @IntentMapping(type = YesIntent.class)
    @WhenSessionAttribute(path = "confirm", hasValues = "new-game")
    public Response confirmPlay(AttributesManager attributes) {
        return startNewGame(attributes);
    }

    @IntentMapping(type = NoIntent.class)
    @WhenSessionAttribute(path = "confirm", hasValues = "new-game")
    public Response confirmNotPlay(AttributesManager attributes) {
        attributes.getSessionAttributes().remove("confirm");

        return Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText(Constants.END_RESPONSE)
                .build())
            .build();
    }

    @IntentMapping(type = NewGame.class)
    public Response onNewGame(AttributesManager attributes) {
        return startNewGame(attributes);
    }

    private Response startNewGame(AttributesManager attributes) {
        GameState state = gameService.newGame();
        gameService.saveGame(attributes, state);

        attributes.getSessionAttributes().remove("confirm");
        attributes.getSessionAttributes().put("state", "playing");

        return Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("Nice, let's start playing. It's player X's turn. You can say a directional word to play, " +
                    "for example north or top, north east or top right.")
                .build())
            .withShouldEndSession(false)
            .build();
    }

    @Priority(MINIMUM) // this method accepts all IntentRequests, so always consider it last
    @RequestMapping(types = IntentRequest.class)
    public Response unhandledIntent(AttributesManager attributesManager) {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("I'm sorry, I don't understand. Do you want to play Tic Tac Toe?")
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
