/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.trivia;

import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.intent.*;
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
