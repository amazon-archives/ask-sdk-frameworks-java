package com.amazon.ask.mvc;

import com.amazon.ask.models.definition.Model;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Simple MVC application do not make use of in-code intent models, so this class simplifies their
 * bootstrap by hiding the {@link SkillModule} logic.
 */
public abstract class SimpleMvcSkillApplication extends MvcSkillApplication {
    @Override
    protected final Map<Locale, String> getInvocationNames() {
        return Collections.emptyMap();
    }

    @Override
    protected final List<SkillModule> getModules() {
        return Collections.singletonList(new SkillModule() {
            @Override
            public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
                SimpleMvcSkillApplication.this.configure(mvcBuilder);
            }

            @Override
            public void buildModel(Model.Builder modelBuilder) {
            }
        });
    }

    /**
     * Override this method to configure the MVC skill (add controllers, view resolvers, etc.)
     *
     * @param builder mvc skill builder
     * @return builder configured with this skill's dependencies
     */
    protected abstract MvcSdkModule.Builder configure(MvcSdkModule.Builder builder);
}
