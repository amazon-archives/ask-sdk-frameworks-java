package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.*;
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.interaction.data.model.SubModel;

import java.io.IOException;
import java.util.*;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders the localized {@link InteractionModel} for a {@link SkillModel}
 */
public class InteractionModelRenderer {
    private final ModelRenderer modelRenderer;

    public InteractionModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = assertNotNull(modelRenderer, "moduleModelRenderer");
    }

    public InteractionModelRenderer() {
        this(new ModelRenderer());
    }

    /**
     * Render localized {@link InteractionModel} for this skill
     *
     * @param skillModel
     * @param locale
     * @return localized skill model
     * @throws IOException if fetching and compiling metadata fails
     */
    public InteractionModel render(SkillModel skillModel, Locale locale) {
        assertNotNull(skillModel, "skillDefinition");
        assertNotNull(locale, "locale");
        if (!skillModel.getInvocationNames().containsKey(locale)) {
            throw new IllegalArgumentException("no invocation name specified for locale: " + locale);
        }

        SubModel subModel = modelRenderer.render(skillModel.getModel(), locale);

        LanguageModel languageModel = LanguageModel.builder()
            .withInvocationName(skillModel.getInvocationNames().get(locale))
            .withIntents(new ArrayList<>(subModel.getLanguageModel().getIntents()))
            .withTypes(new ArrayList<>(subModel.getLanguageModel().getTypes()))
            .build();

        return InteractionModel.builder()
            .withLanguageModel(languageModel)
            .withDialog(subModel.getDialog())
            .withPrompts(subModel.getPrompts())
            .build();
    }
}
