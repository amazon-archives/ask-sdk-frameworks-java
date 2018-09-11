/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.trivia.service;

import com.amazon.ask.trivia.model.Question;
import com.amazon.ask.trivia.model.GameState;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Game {
    private Locale gameLocale;
    private TriviaDataProvider triviaDataProvider;
    private GameState state;

    public Game(Locale gameLocale, TriviaDataProvider triviaDataProvider) {
        this.gameLocale = assertNotNull(gameLocale, "gameLocale");
        this.triviaDataProvider = assertNotNull(triviaDataProvider, "triviaDataProvider");
    }

    public GameState getState() {
        return this.state;
    }

    public void start(int gameLength, int answerCount) {
        List<Question> questions = triviaDataProvider.getData(gameLocale).getQuestions();
        List<Integer> selectedQuestionIndices = selectRandomQuestionIndices(questions.size(), gameLength);
        Question currentQuestion = questions.get(selectedQuestionIndices.get(0));

        this.state = new GameState();
        this.state.setQuestionsIndices(selectedQuestionIndices);
        this.state.setScore(0);
        this.state.setGameLength(gameLength);
        this.state.setAnswerCount(answerCount);

        setQuestionToState(currentQuestion, 0);
    }

    public void load(Map<String, Object> gameStateMap) {
        this.state = new GameState();
        this.state.setQuestionsIndices((List<Integer>)gameStateMap.get("questionsIndices"));
        this.state.setScore((int)gameStateMap.get("score"));
        this.state.setCurrentCorrectAnswer((int)gameStateMap.get("currentCorrectAnswer"));
        this.state.setCurrentCorrectAnswerText((String)gameStateMap.get("currentCorrectAnswerText"));
        this.state.setCurrentAnswers((List<String>)gameStateMap.get("currentAnswers"));
        this.state.setCurrentQuestionIndex((int)gameStateMap.get("currentQuestionIndex"));
        this.state.setCurrentQuestionText((String)gameStateMap.get("currentQuestionText"));
        this.state.setGameLength((int)gameStateMap.get("gameLength"));
        this.state.setAnswerCount((int)gameStateMap.get("answerCount"));
    }

    public boolean submitAnswer(int answerNumber) {
        boolean isAnswerValid = answerNumber > 0 && answerNumber <= this.state.getAnswerCount();
        if (isAnswerValid && answerNumber == this.state.getCurrentCorrectAnswer()) {
            this.state.setScore(this.state.getScore() + 1);
            return true;
        }

        return false;
    }

    public boolean isGameOver() {
        return this.state.getCurrentQuestionIndex() == this.state.getGameLength() - 1;
    }

    public void nextQuestion() {
        if (isGameOver()) {
            throw new IllegalStateException("Game already finished");
        }

        List<Question> questions = triviaDataProvider.getData(gameLocale).getQuestions();
        int nextQuestionIndex = this.state.getCurrentQuestionIndex() + 1;
        Question currentQuestion = questions.get(this.state.getQuestionsIndices().get(nextQuestionIndex));
        setQuestionToState(currentQuestion, nextQuestionIndex);
    }

    private void setQuestionToState(Question currentQuestion, int questionIndex) {
        int correctAnswerIndex = new Random().nextInt(this.state.getAnswerCount());
        List<String> questionAnswers = randomizeAnswers(currentQuestion.getAnswers(), correctAnswerIndex, this.state.getAnswerCount());

        this.state.setCurrentQuestionIndex(questionIndex);
        this.state.setCurrentQuestionText(currentQuestion.getValue());
        this.state.setCurrentAnswers(questionAnswers);
        this.state.setCurrentCorrectAnswer(correctAnswerIndex + 1);
        this.state.setCurrentCorrectAnswerText(questionAnswers.get(correctAnswerIndex));
    }

    private List<Integer> selectRandomQuestionIndices(int max, int count) {
        Random rand = new Random();
        Set<Integer> indices = new HashSet<>();
        while(indices.size() < count) {
            indices.add(rand.nextInt(max));
        }

        return new ArrayList<>(indices);
    }

    /**
     * Randomizes the passed list of answers moving the correct answer to the desired position
     * @param answers list of answers with the correct answer at index 0
     * @param targetCorrectIndex index to move the correct answer to
     * @return List of randomized answers
     */
    private List<String> randomizeAnswers(List<String> answers, int targetCorrectIndex, int answerCount) {
        List<String> answersCopy = new LinkedList<>(answers);
        String correctAnswer = answersCopy.get(0);
        answersCopy.remove(0);

        Collections.shuffle(answersCopy);
        answersCopy = answersCopy.subList(0, answerCount - 1);
        answersCopy.add(targetCorrectIndex, correctAnswer);

        return answersCopy;
    }
}
