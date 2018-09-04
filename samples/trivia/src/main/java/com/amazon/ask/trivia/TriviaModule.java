package com.amazon.ask.trivia;

import com.amazon.ask.models.data.model.IntentData;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.types.intent.*;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.FreeMarkerViewResolver;
import com.amazon.ask.trivia.controllers.TriviaController;
import com.amazon.ask.trivia.intents.AnswerIntent;
import com.amazon.ask.trivia.intents.DontKnowIntent;
import com.amazon.ask.trivia.service.TriviaDataProvider;

/**
 *
 */
public class TriviaModule implements SkillModule {
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
                .withName("intents/start_over_intent")
                .build())
            .intent(RepeatIntent.class)
            .intent(YesIntent.class)
            .intent(NoIntent.class)
            .intent(AnswerIntent.class)
            .intent(DontKnowIntent.class);
    }
}
