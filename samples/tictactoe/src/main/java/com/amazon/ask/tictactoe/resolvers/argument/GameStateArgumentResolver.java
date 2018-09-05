/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
