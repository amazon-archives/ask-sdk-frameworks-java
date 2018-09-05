/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction;

import com.amazon.ask.Skill;
import com.amazon.ask.interaction.definition.SkillModel;

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
