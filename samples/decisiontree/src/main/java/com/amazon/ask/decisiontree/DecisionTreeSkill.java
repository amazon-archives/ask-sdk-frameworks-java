package com.amazon.ask.decisiontree;

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.decisiontree.handlers.exception.CatchAllExceptionHandler;
import com.amazon.ask.decisiontree.handlers.exception.UnhandledSkillExceptionHandler;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DecisionTreeSkill extends MvcSkillApplication {
    @Override
    protected Map<Locale, String> getInvocationNames() {
        return Collections.singletonMap(Locale.forLanguageTag("en-US"), "decision tree");
    }

    @Override
    protected List<SkillModule> getModules() {
        return Collections.singletonList(new DecisionTreeModule());
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledSkillExceptionHandler())
            .addExceptionHandler(new CatchAllExceptionHandler());
    }

}
