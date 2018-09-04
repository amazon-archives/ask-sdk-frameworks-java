package com.amazon.ask.colorpicker;

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.colorpicker.handlers.exception.CatchAllExceptionHandler;
import com.amazon.ask.colorpicker.handlers.exception.UnhandledSkillExceptionHandler;
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;

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
            .addExceptionHandler(new UnhandledSkillExceptionHandler())
            .addExceptionHandler(new CatchAllExceptionHandler());

    }

    @Override
    protected List<SkillModule> getModules() {
        return Collections.singletonList(new ColorPickerModule());
    }
}
