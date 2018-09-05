package com.amazon.ask.tictactoe.resolvers.argument;

import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.tictactoe.service.GameService;
import com.amazon.ask.tictactoe.service.GameState;

import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class GameStateArgumentResolver implements ArgumentResolver {
    private final GameService gameService;

    public GameStateArgumentResolver(GameService gameService) {
        this.gameService = assertNotNull(gameService, "gameService");
    }

    @Override
    public Optional<Object> resolve(ArgumentResolverContext context) {
        if (context.parameterTypeEquals(GameState.class)) {
            return Optional.of(gameService.restoreGame(context.getHandlerInput().getAttributesManager())
                .orElseThrow(() -> new IllegalArgumentException("Could not restore game state from session")));
        }
        return Optional.empty();
    }
}
