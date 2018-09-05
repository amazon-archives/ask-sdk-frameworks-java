package com.amazon.ask.tictactoe.service;

import com.amazon.ask.attributes.AttributesManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertStringNotEmpty;

/**
 * Managers creating and persisting {@link GameState}.
 */
public class GameService {
    private static final String DEFAULT_SESSION_KEY = "game-state";

    private final String sessionKey;

    public GameService() {
        this(DEFAULT_SESSION_KEY);
    }

    public GameService(String sessionKey) {
        this.sessionKey = assertStringNotEmpty(sessionKey, "sessionKey");
    }

    public GameState newGame() {
        GameState state = new GameState();
        state.setCurrentPlayer(Player.X);
        state.setBoard(new HashMap<>());

        return state;
    }

    public Optional<GameState> restoreGame(AttributesManager attributes) {
        Object session = attributes.getSessionAttributes().get(sessionKey);
        if (session == null) {
            return Optional.empty();
        }

        Map map = (Map) session;
        GameState state = new GameState();
        state.setCurrentPlayer(Player.valueOf((String) map.get("currentPlayer")));
        if (map.containsKey("board")) {
            state.setBoard((Map<String, String>) map.get("board"));
        }
        return Optional.of(state);
    }

    public void saveGame(AttributesManager attributes, GameState gameState) {
        attributes.getSessionAttributes().put(sessionKey, gameState);
    }
}
