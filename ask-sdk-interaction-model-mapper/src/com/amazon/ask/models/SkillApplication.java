package com.amazon.ask.models;

import com.amazon.ask.Skill;
import com.amazon.ask.models.definition.SkillModel;

/**
 * Integration point:
 * - enable build tools which generate the interaction model
 * - injection of runtime entry point
 *
 * Unifies a skill's interaction model add its runtime information.
 */
public interface SkillApplication {
    /**
     * @return this skill's definition
     */
    SkillModel getSkillModel();

    /**
     * @return the skill's runtime
     */
    Skill getSkill();
}
