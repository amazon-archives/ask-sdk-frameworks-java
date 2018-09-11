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

import com.amazon.ask.interaction.definition.Model;

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
