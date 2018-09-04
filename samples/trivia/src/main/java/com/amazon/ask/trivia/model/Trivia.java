package com.amazon.ask.trivia.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Trivia {
    private Locale locale;
    private List<Question> questions;

    public Trivia() {
        this(Locale.ENGLISH, new ArrayList<>());
    }

    public Trivia(Locale locale) {
        this(locale , new ArrayList<>());
    }

    public Trivia(Locale locale, List<Question> questions) {
        this.locale = locale;
        this.questions = questions;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
