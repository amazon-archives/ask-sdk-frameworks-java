package com.amazon.ask.tictactoe;

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.tictactoe.handlers.exception.CatchAllExceptionHandler;
import com.amazon.ask.tictactoe.handlers.exception.UnhandledSkillExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TicTacToe extends MvcSkillApplication {

    @Override
    protected Map<Locale, String> getInvocationNames() {
        return Collections.singletonMap(Locale.US, "tic tac toe");
    }

    @Override
    protected List<SkillModule> getModules() {
        return Collections.singletonList(new TicTacToeModule());
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledSkillExceptionHandler())
            .addExceptionHandler(new CatchAllExceptionHandler());
    }

}