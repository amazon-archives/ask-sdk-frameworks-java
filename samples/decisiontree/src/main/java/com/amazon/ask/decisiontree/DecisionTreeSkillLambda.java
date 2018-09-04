package com.amazon.ask.decisiontree;

import com.amazon.ask.SkillStreamHandler;

/**
 * Adapts the {@link DecisionTreeSkill} to run within lambda.
 */
public class DecisionTreeSkillLambda extends SkillStreamHandler {
    public DecisionTreeSkillLambda() {
        super(new DecisionTreeSkill().getSkill());
    }
}
