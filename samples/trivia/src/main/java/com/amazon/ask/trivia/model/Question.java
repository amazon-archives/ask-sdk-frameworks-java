package com.amazon.ask.trivia.model;

import java.util.List;

public class Question {
    private String text;
    private List<String> answers;

    public Question(String text, List<String> answers) {
        this.text = text;
        this.answers = answers;
    }

    public String getValue() {
        return text;
    }

    public void setValue(String text) {
        this.text = text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
