package com.amazon.ask.colorpicker;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.interaction.SkillApplication;

/**
 * Adapts the {@link ColorPickerSkill} for running within a Lambad environment.
 */
public class ColorPickerSkillLambda extends SkillStreamHandler {
    public ColorPickerSkillLambda() {
        this(new ColorPickerSkill());
    }

    public ColorPickerSkillLambda(SkillApplication skillApplication) {
        super(skillApplication.getSkill());
    }
}
