package com.amazon.ask.tictactoe.controllers;

import com.amazon.ask.attributes.AttributesManager;
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
import com.amazon.ask.tictactoe.game.GameService;
import com.amazon.ask.tictactoe.game.GameState;
import com.amazon.ask.tictactoe.game.MoveResult;
import com.amazon.ask.tictactoe.game.Player;
import com.amazon.ask.tictactoe.intents.NewGame;
import com.amazon.ask.tictactoe.intents.PlayMove;

import java.util.HashMap;
import java.util.Map;

import static com.amazon.ask.mvc.mapper.Priority.MINIMUM;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Controller for when game is in the 'playing' state.
 */
@WhenSessionAttribute(path = "state", hasValues = "playing")
public class PlayingController {
    private final GameService gameService;

    public PlayingController(GameService gameService) {
        this.gameService = assertNotNull(gameService, "gameService");
    }

    @IntentMapping(type = PlayMove.class)
    @WhenSessionAttribute(path = "state", hasValues = "playing")
    public ModelAndView playMove(PlayMove move, AttributesManager attributesManager) {
        GameState game = gameService.restoreGame(attributesManager)
            .orElseThrow(() -> new IllegalStateException("Could not restore game state."));

        Player currentPlayer = game.getCurrentPlayer();
        Player nextPlayer = currentPlayer == Player.X ? Player.O : Player.X;
        MoveResult result = game.playMove(move, currentPlayer);

        Map<String, Object> model = new HashMap<>();
        model.put("square", move.getSquare().name());
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
        GameState gameState = gameService.newGame();
        gameService.saveGame(attributesManager, gameState);
        attributesManager.getSessionAttributes().remove("confirm");

        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("Ok, I reset the game for you. Player x to start.")
                .build())
            .build();
    }

    @IntentMapping(type = NoIntent.class)
    @WhenSessionAttribute(path = "confirm", hasValues = "new-game")
    public Response noNewGame(AttributesManager attributesManager) {
        GameState gameState = gameService.restoreGame(attributesManager)
            .orElseThrow(() -> new IllegalStateException("Could not restore game state."));

        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("Ok. It is Player " + gameState.getCurrentPlayer().name() + "'s turn.")
                .build())
            .build();
    }

    @Priority(MINIMUM) // this method accepts all IntentRequests, so consider it last
    @RequestMapping(types = IntentRequest.class)
    public Response unhandledIntent(AttributesManager attributesManager) {
        GameState gameState = gameService.restoreGame(attributesManager)
            .orElseThrow(() -> new IllegalStateException("Could not restore game state."));

        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("I'm sorry, I don't understand. It's your turn, Player " + gameState.getCurrentPlayer().name() + ".")
                .build())
            .build();
    }
}
