package com.amazon.ask.trivia;

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.trivia.handlers.exception.CatchAllExceptionHandler;
import com.amazon.ask.trivia.handlers.exception.UnhandledSkillExceptionHandler;

import java.util.*;

public class TriviaSkill extends MvcSkillApplication {

    @Override
    protected Map<Locale, String> getInvocationNames() {
        Map<Locale, String> invocationNames = new HashMap<>();
        invocationNames.put(Locale.forLanguageTag("en-US"), "reindeer trivia");
        invocationNames.put(Locale.forLanguageTag("en-GB"), "game");
        invocationNames.put(Locale.forLanguageTag("de-DE"), "spiel");
        return invocationNames;
    }

    @Override
    protected List<SkillModule> getModules() {
        return Collections.singletonList(new TriviaModule());
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledSkillExceptionHandler())
            .addExceptionHandler(new CatchAllExceptionHandler());
    }
}
