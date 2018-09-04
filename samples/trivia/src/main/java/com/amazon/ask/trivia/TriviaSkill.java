package com.amazon.ask.trivia;

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.data.source.ResourceSource;
import com.amazon.ask.models.definition.IntentDefinition;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.types.intent.*;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.FreeMarkerViewResolver;
import com.amazon.ask.trivia.interaction.AnswerIntent;
import com.amazon.ask.trivia.interaction.DontKnowIntent;

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
        return Collections.singletonList(new SkillModule() {
            @Override
            public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
                mvcBuilder
                    .addControllers(new TriviaController(new TriviaDataProvider()))
                    .addViewResolvers(FreeMarkerViewResolver.builder()
                        .withPrefix("/com/amazon/ask/trivia/views/")
                        .build());
            }

            @Override
            public void buildModel(Model.Builder modelBuilder) {
                modelBuilder
                    .intent(CancelIntent.class)
                    .intent(HelpIntent.class)
                    .intent(StopIntent.class)
                    .intent(StartOverIntent.class, IntentData.resource() // extend built-in intent samples
                        .withResourceClass(getClass())
                        .withName("interaction/models/start_over_intent")
                        .build())
                    .intent(RepeatIntent.class)
                    .intent(YesIntent.class)
                    .intent(NoIntent.class)
                    .intent(AnswerIntent.class)
                    .intent(DontKnowIntent.class);
            }
        });
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledRequestHandler());
//            .addExceptionHandler(new ExceptionRequestHandler());
    }
}
