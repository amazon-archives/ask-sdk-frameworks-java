/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.decisiontree;

import com.amazon.ask.decisiontree.controllers.DecisionTreeController;
import com.amazon.ask.decisiontree.intents.CouchPotatoIntent;
import com.amazon.ask.decisiontree.intents.RecommendationIntent;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.nashorn.NashornViewResolver;

/**
 *
 */
public class DecisionTreeModule implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        mvcBuilder
            .addController(new DecisionTreeController())
            .addViewResolver(NashornViewResolver.builder()
                .withPrefix("/com/amazon/ask/decisiontree/views/")
                .withResourceClass(getClass())
                .withRenderFunction("render")
                .build());
    }

    @Override
    public void buildModel(Model.Builder modelBuilder) {
        modelBuilder
            .intent(CancelIntent.class)
            .intent(HelpIntent.class)
            .intent(StopIntent.class)
            .intent(CouchPotatoIntent.class)
            .intent(RecommendationIntent.class);
    }
}
