package com.amazon.ask.trivia.model;

import java.util.List;

public class GameState {
    private List<Integer> questionsIndices;
    private int currentQuestionIndex;
    private String currentQuestionText;
    private List<String> currentAnswers;
    private int currentCorrectAnswer;
    private String currentCorrectAnswerText;
    private int score;
    private int answerCount;
    private int gameLength;

    public String getCurrentQuestionText() {
        return currentQuestionText;
    }

    public void setCurrentQuestionText(String currentQuestionText) {
        this.currentQuestionText = currentQuestionText;
    }

    public List<String> getCurrentAnswers() {
        return currentAnswers;
    }

    public void setCurrentAnswers(List<String> currentAnswers) {
        this.currentAnswers = currentAnswers;
    }

    public int getCurrentCorrectAnswer() {
        return currentCorrectAnswer;
    }

    public void setCurrentCorrectAnswer(int currentCorrectAnswer) {
        this.currentCorrectAnswer = currentCorrectAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Integer> getQuestionsIndices() {
        return questionsIndices;
    }

    public void setQuestionsIndices(List<Integer> questionsIndices) {
        this.questionsIndices = questionsIndices;
    }

    public String getCurrentCorrectAnswerText() {
        return currentCorrectAnswerText;
    }

    public void setCurrentCorrectAnswerText(String currentCorrectAnswerText) {
        this.currentCorrectAnswerText = currentCorrectAnswerText;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
}
