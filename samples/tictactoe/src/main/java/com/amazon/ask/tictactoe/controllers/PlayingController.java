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
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.interaction.types.intent.NoIntent;
import com.amazon.ask.interaction.types.intent.YesIntent;
import com.amazon.ask.mvc.annotation.argument.SessionAttributes;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.mapper.Priority;
import com.amazon.ask.mvc.view.ModelAndView;
import com.amazon.ask.tictactoe.service.GameService;
import com.amazon.ask.tictactoe.service.GameState;
import com.amazon.ask.tictactoe.service.MoveResult;
import com.amazon.ask.tictactoe.service.Player;
import com.amazon.ask.tictactoe.intents.NewGame;
import com.amazon.ask.tictactoe.intents.PlayMove;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static com.amazon.ask.mvc.mapper.Priority.MINIMUM;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Controller for when game is in the 'playing' state.
 */
@WhenSessionAttribute(path = "state", hasValues = "playing")
public class PlayingController {
    private final GameService gameService;

    private final String HELP_RESPONSE = "You can say a directional word to play, for example north or top, north east or top right.";

    public PlayingController(GameService gameService) {
        this.gameService = assertNotNull(gameService, "gameService");
    }

    @IntentMapping(type = PlayMove.class)
    @WhenSessionAttribute(path = "state", hasValues = "playing")
    public ModelAndView playMove(PlayMove move, AttributesManager attributesManager, Locale locale) {
        GameState game = gameService.restoreGame(attributesManager)
            .orElseThrow(() -> new IllegalStateException("Could not restore game state."));

        Player currentPlayer = game.getCurrentPlayer();
        Player nextPlayer = currentPlayer == Player.X ? Player.O : Player.X;
        MoveResult result = game.playMove(move, currentPlayer);

        Map<String, Object> model = new HashMap<>();
        ResourceBundle messages = ResourceBundle.getBundle("com.amazon.ask.tictactoe.responses.SquareNames", locale);
        model.put("square", messages.getString(move.getSquare().name()));
        model.put("currentPlayer", game.getCurrentPlayer().name());
        model.put("nextPlayer", nextPlayer.name());
        model.put("result", result.name());

        if (result == MoveResult.LEGAL) {
            game.setCurrentPlayer(nextPlayer);
            gameService.saveGame(attributesManager, game);
        }

        return new ModelAndView("play_move", model);
    }

    @IntentMapping(type = NewGame.class)
    public Response newGame(@SessionAttributes Map<String, Object> session) {
        session.put("confirm", "new-game");
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("You are already playing a game. Are you sure you want to start a new game?")
                .build())
            .build();
    }

    @IntentMapping(type = YesIntent.class)
    @WhenSessionAttribute(path = "confirm", hasValues = "new-game")
    public Response yesNewGame(AttributesManager attributesManager) {
        attributesManager.getSessionAttributes().remove("confirm");

        GameState gameState = gameService.newGame();
        gameService.saveGame(attributesManager, gameState);

        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("Ok, I reset the game for you. Player X to start first.")
                .build())
            .build();
    }

    @IntentMapping(type = NoIntent.class)
    @WhenSessionAttribute(path = "confirm", hasValues = "new-game")
    public Response noNewGame(GameState gameState) {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("Ok. Let's keep playing. It is Player " + gameState.getCurrentPlayer().name() + "'s turn.")
                .build())
            .build();
    }

    @IntentMapping(type = HelpIntent.class)
    public Response help(GameState gameState) {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("It is Player " + gameState.getCurrentPlayer().name() + "'s turn. " + HELP_RESPONSE)
                .build())
            .build();
    }

    @Priority(MINIMUM) // this method accepts all IntentRequests, so consider it last
    @RequestMapping(types = IntentRequest.class)
    public Response unhandledIntent(GameState gameState) {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("I'm sorry, I don't understand. It's your turn, Player " + gameState.getCurrentPlayer().name() + ". You can say help for instruction.")
                .build())
            .build();
    }
}
