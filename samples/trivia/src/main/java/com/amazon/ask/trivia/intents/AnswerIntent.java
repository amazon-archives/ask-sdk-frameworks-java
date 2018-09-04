package com.amazon.ask.trivia.intents;

import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.types.slot.AmazonNumber;

@Intent("AnswerIntent")
@IntentResource("answer_intent")
public class AnswerIntent {
  private AmazonNumber answer;

  public AmazonNumber getAnswer() {
    return answer;
  }

  public void setAnswer(AmazonNumber answer) {
    this.answer = answer;
  }
}
