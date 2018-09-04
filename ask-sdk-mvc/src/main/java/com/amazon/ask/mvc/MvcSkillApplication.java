package com.amazon.ask.mvc;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.interaction.SkillApplication;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.definition.SkillModel;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Application composed from multiple {@link SkillModule}s
 */
public abstract class MvcSkillApplication implements SkillApplication {

    /**
     * Lazily initializes the {@link Skill} from {@link SkillModule}.
     */
    @Override
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
     */
    protected SkillBuilder getSkillBuilder() {
        return Skills.standard();
    }

    /**
     * Merges modules into a single {@link SkillModel}
     *
     * @return skill definition containing all module's interaction models
     */
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
