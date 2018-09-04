package com.amazon.ask.decisiontree;

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.types.intent.CancelIntent;
import com.amazon.ask.models.types.intent.HelpIntent;
import com.amazon.ask.models.types.intent.StopIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.nashorn.NashornViewResolver;
import com.amazon.ask.decisiontree.interaction.CouchPotatoIntent;
import com.amazon.ask.decisiontree.interaction.RecommendationIntent;

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
        return Collections.singletonList(new SkillModule() {
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
        });
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledRequestHandler())
            .addExceptionHandler(new ExceptionRequestHandler());
    }

}
