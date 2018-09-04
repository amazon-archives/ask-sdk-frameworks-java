package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.model.InteractionModel;
import com.amazon.ask.interaction.model.SkillModel;

import java.util.Locale;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Associate a {@link InteractionModel} add a {@link Locale}
 */
public class LocalizedInteractionModel {
    private final SkillModel skillModel;
    private final Locale locale;

    public LocalizedInteractionModel(SkillModel skillModel, Locale locale) {
        this.skillModel = assertNotNull(skillModel, "skillModel");
        this.locale = assertNotNull(locale, "locale");
    }

    public SkillModel getSkillModel() {
        return skillModel;
    }

    public Locale getLocale() {
        return locale;
    }
}
