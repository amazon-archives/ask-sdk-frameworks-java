package com.amazon.ask.colorpicker;

import com.amazon.ask.colorpicker.controllers.ColorPickerController;
import com.amazon.ask.colorpicker.intents.MyColorIsIntent;
import com.amazon.ask.colorpicker.intents.WhatsMyColorIntent;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.types.intent.CancelIntent;
import com.amazon.ask.models.types.intent.HelpIntent;
import com.amazon.ask.models.types.intent.StopIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.FreeMarkerViewResolver;

/**
 *
 */
public class ColorPickerModule implements SkillModule {
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
}
