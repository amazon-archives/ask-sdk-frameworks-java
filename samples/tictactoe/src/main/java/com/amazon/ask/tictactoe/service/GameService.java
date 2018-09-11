/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.tictactoe.service;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.tictactoe.model.GameState;
import com.amazon.ask.tictactoe.model.Player;

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
