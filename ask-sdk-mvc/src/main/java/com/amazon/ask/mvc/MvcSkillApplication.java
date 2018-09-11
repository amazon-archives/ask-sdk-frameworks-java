/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.interaction.build.SkillModelSupplier;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.definition.SkillModel;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Application composed from multiple {@link SkillModule}s
 */
public abstract class MvcSkillApplication implements SkillModelSupplier {

    /**
     * @return builds the {@link Skill} from its modules {@link SkillModule}.
     */
    public Skill getSkill() {
        List<SkillModule> modules = getModules();
        // Let all modules configure their MVC runtime
        MvcSdkModule.Builder builder = MvcSdkModule.builder();
        for (SkillModule module : modules) {
            module.buildMvc(builder);
        }

        // Accumulate all models from the modules and build the whole SkillModel
        builder.withModel(buildSkillModel(modules).getModel());

        MvcSdkModule sdkModule = builder.build();
        return getSkillBuilder()
            .registerSdkModule(sdkModule)
            .build();
    }

    /**
     * Override this to customize your Skill configuration.
     *
     * @return the skill builder
     */
    protected SkillBuilder getSkillBuilder() {
        return Skills.standard();
    }

    /**
     * Merges modules into a single {@link SkillModel}
     *
     * @return skill definition containing all module's interaction models
     */
    @Override
    public SkillModel getSkillModel() {
        return buildSkillModel(getModules());
    }

    protected SkillModel buildSkillModel(List<SkillModule> modules) {
        Model.Builder builder = getModelBuilder();
        modules.forEach(m -> m.buildModel(builder));

        return SkillModel.builder()
            .withInvocationNames(getInvocationNames())
            .addModel(builder.build())
            .build();
    }

    protected Model.Builder getModelBuilder() {
        return Model.builder();
    }

    /**
     * @return invocation names for each supported locale
     */
    protected abstract Map<Locale, String> getInvocationNames();

    /**
     * @return list of this skill's modules
     */
    protected abstract List<SkillModule> getModules();
}
