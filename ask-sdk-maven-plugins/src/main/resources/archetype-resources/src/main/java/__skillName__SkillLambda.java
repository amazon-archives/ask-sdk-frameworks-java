package ${package};

import com.amazon.ask.SkillStreamHandler;

/**
 * Adapts the {@link ${skillName}Skill} for running within a Lambad environment.
 */
public class ${skillName}SkillLambda extends SkillStreamHandler {
    public ${skillName}SkillLambda() {
        super(new ${skillName}Skill().getSkill());
    }
}
