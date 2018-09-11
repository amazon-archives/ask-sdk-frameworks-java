/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.tictactoe;

import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.intent.NoIntent;
import com.amazon.ask.interaction.types.intent.YesIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.nashorn.NashornViewResolver;
import com.amazon.ask.tictactoe.controllers.GlobalController;
import com.amazon.ask.tictactoe.controllers.MenuController;
import com.amazon.ask.tictactoe.controllers.PlayingController;
import com.amazon.ask.tictactoe.resolvers.argument.GameStateArgumentResolver;
import com.amazon.ask.tictactoe.service.GameService;
import com.amazon.ask.tictactoe.intents.NewGame;
import com.amazon.ask.tictactoe.intents.PlayMove;

public class TicTacToeModule implements SkillModule {

    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        GameService gameService = new GameService();
        mvcBuilder
            .addController(new GlobalController())
            .addController(new MenuController(gameService))
            .addController(new PlayingController(gameService))
            .addViewResolver(NashornViewResolver.builder()
                .withResourceClass(getClass())
                .withPrefix("views/")
                .withRenderFunction("render")
                .build())
            .addArgumentResolver(new GameStateArgumentResolver(gameService));
    }

    @Override
    public void buildModel(Model.Builder modelBuilder) {
        modelBuilder
            .intent(YesIntent.class)
            .intent(NoIntent.class)
            .intent(NewGame.class)
            .intent(PlayMove.class);
    }
}