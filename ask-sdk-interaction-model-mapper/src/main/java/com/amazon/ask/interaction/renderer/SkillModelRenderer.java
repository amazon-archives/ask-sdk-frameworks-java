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

import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.interaction.model.InteractionModelEnvelope;

import java.util.Locale;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders a {@link SkillModel}'s {@link InteractionModelEnvelope}.
 */
public class SkillModelRenderer {
    private final InteractionModelRenderer renderer;

    public SkillModelRenderer(InteractionModelRenderer renderer) {
        this.renderer = assertNotNull(renderer, "renderer");
    }

    public SkillModelRenderer() {
        this(new InteractionModelRenderer());
    }

    public InteractionModelEnvelope render(SkillModel skillModel, Locale locale) {
        return InteractionModelEnvelope.builder()
            .withInteractionModel(renderer.render(skillModel, locale))
            .build();
    }
}
