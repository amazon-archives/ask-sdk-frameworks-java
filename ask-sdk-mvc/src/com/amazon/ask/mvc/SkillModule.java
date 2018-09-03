package com.amazon.ask.mvc;

import com.amazon.ask.models.definition.Model;

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
     * @return updated builder
     */
    void buildMvc(MvcSdkModule.Builder mvcBuilder);

    /**
     * Install's this module's interaction model requirements into
     *
     * @param modelBuilder builder to add interaction model data into
     */
    void buildModel(Model.Builder modelBuilder);
}
