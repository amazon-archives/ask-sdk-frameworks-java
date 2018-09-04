package com.amazon.ask.trivia.interaction;

import com.amazon.ask.models.annotation.data.IntentResource;
import com.amazon.ask.models.annotation.type.Intent;
import com.amazon.ask.models.types.slot.AmazonNumber;

@Intent("AnswerIntent")
@IntentResource("models/answer_intent")
public class AnswerIntent {
  private AmazonNumber answer;

  public AmazonNumber getAnswer() {
    return answer;
  }

  public void setAnswer(AmazonNumber answer) {
    this.answer = answer;
  }
}
