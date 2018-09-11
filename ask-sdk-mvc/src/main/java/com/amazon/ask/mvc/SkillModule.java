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

/**
 * Interface for a skill module, containing both an interaction model and runtime logic.
 *
 * The runtime and interaction model are exposed as separate methods so that the interaction model
 * can be rendered from a sandboxed or restrictive environment. E.g the skill's build and
 * deployment environments vs the web servers or lambda functions serving traffic.
 *
 * This is because the {@link MvcSdkModule} constructs the controllers which may open database
 * connections or other resources outside the scope of a build/deploy fleet.
 */
public interface SkillModule {
    /**
     * Install this module's runtime handlers into the {@link MvcSdkModule.Builder}
     *
     * @param mvcBuilder mvc sdk module builder
     */
    void buildMvc(MvcSdkModule.Builder mvcBuilder);

    /**
     * Install's this module's interaction model requirements into
     *
     * @param modelBuilder builder to add interaction model data into
     */
    void buildModel(Model.Builder modelBuilder);
}
