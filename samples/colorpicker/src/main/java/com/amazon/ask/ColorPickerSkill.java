package com.amazon.ask.colorpicker;

import com.amazon.ask.colorpicker.interaction.MyColorIsIntent;
import com.amazon.ask.colorpicker.interaction.WhatsMyColorIntent;
import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.definition.SkillModel;
import com.amazon.ask.models.types.intent.CancelIntent;
import com.amazon.ask.models.types.intent.HelpIntent;
import com.amazon.ask.models.types.intent.StopIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.FreeMarkerViewResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Entry point for a Lambda Skill, defines the MVC controllers and {@link SkillModel}
 */
public class ColorPickerSkill extends MvcSkillApplication {
    @Override
    protected Map<Locale, String> getInvocationNames() {
        return Collections.singletonMap(Locale.forLanguageTag("en-US"), "color picker");
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledRequestHandler())
            .addExceptionHandler(new ExceptionRequestHandler());

    }

    @Override
    protected List<SkillModule> getModules() {
        return Collections.singletonList(new SkillModule() {

            @Override
            public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
                mvcBuilder
                    .addController(new ColorPickerController())
                    .addViewResolvers(FreeMarkerViewResolver.builder()
                        .withResourceClass(ColorPickerSkill.class)
                        .withPrefix("views/")
                        .build());
            }

            @Override
            public void buildModel(Model.Builder modelBuilder) {
                modelBuilder
                    .intent(CancelIntent.class)
                    .intent(HelpIntent.class)
                    .intent(MyColorIsIntent.class)
                    .intent(StopIntent.class)
                    .intent(WhatsMyColorIntent.class);
            }
        });
    }

}
