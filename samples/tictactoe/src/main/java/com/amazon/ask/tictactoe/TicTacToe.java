package com.amazon.ask.tictactoe;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
                .addExceptionHandler(new ExceptionRequestHandler());
    }

}