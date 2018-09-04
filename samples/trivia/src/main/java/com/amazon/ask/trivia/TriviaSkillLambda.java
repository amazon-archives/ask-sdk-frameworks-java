package com.amazon.ask.trivia;

import com.amazon.ask.SkillStreamHandler;

/**
 * Adapts the {@link TriviaSkill} to run within lambda.
 */
public class TriviaSkillLambda extends SkillStreamHandler {

    public TriviaSkillLambda() { super(new TriviaSkill().getSkill()); }
}
