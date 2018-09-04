package com.amazon.ask.tictactoe;

import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.intent.NoIntent;
import com.amazon.ask.interaction.types.intent.YesIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.nashorn.NashornViewResolver;
import com.amazon.ask.tictactoe.controllers.MenuController;
import com.amazon.ask.tictactoe.controllers.PlayingController;
import com.amazon.ask.tictactoe.game.GameService;
import com.amazon.ask.tictactoe.intents.NewGame;
import com.amazon.ask.tictactoe.intents.PlayMove;

public class TicTacToeModule implements SkillModule {

    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        GameService gameService = new GameService();
        mvcBuilder
            .addController(new MenuController(gameService))
            .addController(new PlayingController(gameService))
            .addViewResolver(NashornViewResolver.builder()
                .withResourceClass(getClass())
                .withPrefix("views/")
                .withRenderFunction("render")
                .build());
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