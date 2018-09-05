/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
