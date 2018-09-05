/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
     * @param skillModel skill model containing invocation names and models
     * @param locale alexa locale to render a model for
     * @return localized skill model
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
