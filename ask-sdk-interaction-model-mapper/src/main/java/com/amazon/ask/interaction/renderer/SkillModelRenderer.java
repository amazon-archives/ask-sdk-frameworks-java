package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.definition.SkillModel;

import java.util.Locale;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders a {@link SkillModel}'s {@link com.amazon.ask.interaction.model.SkillModel}.
 */
public class SkillModelRenderer {
    private final InteractionModelRenderer renderer;

    public SkillModelRenderer(InteractionModelRenderer renderer) {
        this.renderer = assertNotNull(renderer, "renderer");
    }

    public SkillModelRenderer() {
        this(new InteractionModelRenderer());
    }

    public com.amazon.ask.interaction.model.SkillModel render(SkillModel skillModel, Locale locale) {
        return com.amazon.ask.interaction.model.SkillModel.builder()
            .withInteractionModel(renderer.render(skillModel, locale))
            .build();
    }
}
