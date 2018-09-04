package com.amazon.ask.decisiontree;

import com.amazon.ask.decisiontree.controllers.DecisionTreeController;
import com.amazon.ask.decisiontree.intents.CouchPotatoIntent;
import com.amazon.ask.decisiontree.intents.RecommendationIntent;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.types.intent.CancelIntent;
import com.amazon.ask.models.types.intent.HelpIntent;
import com.amazon.ask.models.types.intent.StopIntent;
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
