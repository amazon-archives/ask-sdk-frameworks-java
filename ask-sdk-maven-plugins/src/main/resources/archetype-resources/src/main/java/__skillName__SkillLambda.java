package ${package};

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.interaction.SkillApplication;

/**
 * Adapts the {@link ${skillName}Skill} for running within a Lambad environment.
 */
public class ${skillName}SkillLambda extends SkillStreamHandler {
    public ${skillName}SkillLambda() {
        this(new ${skillName}Skill());
    }

    public ${skillName}SkillLambda(SkillApplication skillApplication) {
        super(skillApplication.getSkill());
    }
}
