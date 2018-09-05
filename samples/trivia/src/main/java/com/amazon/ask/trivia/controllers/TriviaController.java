/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.trivia.controllers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.interaction.types.intent.*;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.mvc.annotation.mapping.ExceptionHandler;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.view.ModelAndView;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import com.amazon.ask.trivia.TriviaSkill;
import com.amazon.ask.trivia.service.Game;
import com.amazon.ask.trivia.service.TriviaDataProvider;
import com.amazon.ask.trivia.intents.AnswerIntent;
import com.amazon.ask.trivia.intents.DontKnowIntent;

public class TriviaController {
    private static final int ANSWER_COUNT = 4;
    private static final int GAME_LENGTH = 5;
    private static final String GAME_KEY = "CURRENT_GAME";
    private static final String STATE_KEY = "SKILL_STATE";
    private static final String STARTED_STATE = "GAME_STARTED";
    private TriviaDataProvider triviaDataProvider;

    public TriviaController(TriviaDataProvider triviaDataProvider) {
        this.triviaDataProvider = triviaDataProvider;
    }

    @ExceptionHandler
    public Optional<Response> handleException(HandlerInput handlerInput, Throwable throwable) {
        // TODO: Use a view?
        System.out.println("Exception: " + throwable.toString());
        System.out.println("Exception thrown while receiving: " + handlerInput.getRequestEnvelope().getRequest().getType());
        return handlerInput.getResponseBuilder()
            .withSpeech("Sorry. I have problems answering your request. Please try again")
            .withShouldEndSession(true)
            .build();
    }

    @RequestMapping(LaunchRequest.class)
    public ModelAndView launch(Locale locale, AttributesManager attributesManager) {
        return startGame(true, locale, attributesManager);
    }

    @RequestMapping(SessionEndedRequest.class)
    public Response sessionEnded(SessionEndedRequest sessionEndedRequest) {
        System.out.println("Session ended with " + sessionEndedRequest.getReason().toString() + " reason.");
        return Response.builder().build();
    }

    @IntentMapping(type = StartOverIntent.class)
    public ModelAndView startOverIntent(Locale locale, AttributesManager attributesManager) {
        return startGame(true, locale, attributesManager);
    }

    @IntentMapping(type = AnswerIntent.class)
    public ModelAndView answerIntent(Locale locale, AnswerIntent intent, AttributesManager attributesManager) {
        return userAnswer(false, locale, intent.getAnswer().getNumber(), attributesManager);
    }

    @IntentMapping(type = DontKnowIntent.class)
    public ModelAndView dontKnowIntent(Locale locale, AttributesManager attributesManager) {
        return userAnswer(true, locale, null, attributesManager);
    }

    @IntentMapping(type = HelpIntent.class)
    @WhenSessionAttribute(path = STATE_KEY, hasValues = {}, matchNull = true)
    public ModelAndView helpStartIntent(Locale locale) {
        ResourceBundle messages = getMessages(locale);
        String askMessage = messages.getString("ASK_MESSAGE_START");
        String helpMessage = String.format(messages.getString("HELP_MESSAGE"), GAME_LENGTH);
        String helpReprompt = messages.getString("HELP_REPROMPT");

        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", helpMessage + " " + askMessage);
        mv.put("reprompt", helpReprompt + " " + askMessage);
        return mv;
    }

    @IntentMapping(type = HelpIntent.class)
    @WhenSessionAttribute(path = STATE_KEY, hasValues = {STARTED_STATE})
    public ModelAndView helpIntent(Locale locale) {
        ResourceBundle messages = getMessages(locale);
        String askMessage = messages.getString("REPEAT_QUESTION_MESSAGE") + " " + messages.getString("STOP_MESSAGE");
        String helpMessage = String.format(messages.getString("HELP_MESSAGE"), GAME_LENGTH);
        String helpReprompt = messages.getString("HELP_REPROMPT");

        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", helpMessage + " " + askMessage);
        mv.put("reprompt", helpReprompt + " " + askMessage);
        return mv;
    }

    @IntentMapping(type = RepeatIntent.class)
    public ModelAndView repeatIntent(AttributesManager attributesManager) {
        return repeatFromSession(attributesManager.getSessionAttributes());
    }

    @IntentMapping(type = YesIntent.class)
    @WhenSessionAttribute(path = STATE_KEY, hasValues = {}, matchNull = true)
    public ModelAndView yesStartIntent(Locale locale, AttributesManager attributesManager) {
        return startGame(false, locale, attributesManager);
    }

    @IntentMapping(type = YesIntent.class)
    @WhenSessionAttribute(path = STATE_KEY, hasValues = {STARTED_STATE})
    public ModelAndView yesIntent(AttributesManager attributesManager) {
        return repeatFromSession(attributesManager.getSessionAttributes());
    }

    @IntentMapping(type = StopIntent.class)
    public ModelAndView stopIntent(Locale locale) {
        return exit(locale);
    }

    @IntentMapping(type = NoIntent.class)
    public ModelAndView noIntent(Locale locale) {
        ResourceBundle messages = getMessages(locale);
        String noMessage = messages.getString("NO_MESSAGE");

        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", noMessage);

        return mv;
    }

    @IntentMapping(type = CancelIntent.class)
    public ModelAndView cancelIntent(Locale locale) {
        return exit(locale);
    }

    private ModelAndView exit(Locale locale) {
        ResourceBundle messages = getMessages(locale);
        String cancelMessage = messages.getString("CANCEL_MESSAGE");

        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", cancelMessage);

        return mv;
    }

    private ModelAndView startGame(boolean greeting, Locale locale, AttributesManager attributesManager) {
        Game game = getGame(locale);
        game.start(GAME_LENGTH, ANSWER_COUNT);

        ResourceBundle messages = getMessages(locale);

        String welcomeMessage = "";
        if(greeting) {
            welcomeMessage += String.format(messages.getString("NEW_GAME_MESSAGE"), messages.getString("GAME_NAME")) + " ";
            welcomeMessage += String.format(messages.getString("WELCOME_MESSAGE"), GAME_LENGTH) + " ";
        }

        String tellQuestionMessage = getTellQuestionMessage(messages, game);
        welcomeMessage += tellQuestionMessage;

        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", welcomeMessage);
        mv.put("reprompt", tellQuestionMessage);
        mv.put("card_title", messages.getString("GAME_NAME"));

        Map<String, Object> session = attributesManager.getSessionAttributes();
        session.put(GAME_KEY, game.getState());
        session.put(STATE_KEY, STARTED_STATE);
        session.put("speechText", tellQuestionMessage);
        session.put("repromptText", tellQuestionMessage);
        session.put("cardTitleText", messages.getString("GAME_NAME"));
        attributesManager.setSessionAttributes(session);

        return mv;
    }

    private ModelAndView userAnswer(boolean isUserGivingUp, Locale locale, Long answerNumber, AttributesManager attributesManager) {
        StringBuilder speechAnalysis = new StringBuilder();
        StringBuilder speech = new StringBuilder();
        ResourceBundle messages = getMessages(locale);

        Game game = getGame(locale);
        game.load(((Map<String,Object>)attributesManager.getSessionAttributes().get(GAME_KEY)));
        boolean isAnswerCorrect = !isUserGivingUp && answerNumber != null && game.submitAnswer(answerNumber.intValue());

        if (!isUserGivingUp) {
            speechAnalysis.append(messages.getString("ANSWER_IS_MESSAGE"));
            speechAnalysis.append(" ");
            if (isAnswerCorrect) {
                speechAnalysis.append(messages.getString("ANSWER_CORRECT_MESSAGE"));
            } else {
                speechAnalysis.append(messages.getString("ANSWER_WRONG_MESSAGE"));
            }
            speechAnalysis.append(" ");
        }

        if (!isAnswerCorrect) {
            speechAnalysis.append(String.format(messages.getString("CORRECT_ANSWER_MESSAGE"),
                game.getState().getCurrentCorrectAnswer(),
                game.getState().getCurrentCorrectAnswerText()));
        }

        if (game.isGameOver()) {
            speech.append(speechAnalysis.toString());
            speech.append(" ");
            speech.append(String.format(messages.getString("GAME_OVER_MESSAGE"), game.getState().getScore(),
                GAME_LENGTH));

            ModelAndView mv = new ModelAndView("generic");
            mv.put("speech", speech.toString());

            return mv;
        }

        game.nextQuestion();

        String tellQuestionMessage = getTellQuestionMessage(messages, game);

        speech.append(speechAnalysis.toString());
        speech.append(" ");
        speech.append(String.format(messages.getString("SCORE_IS_MESSAGE"), game.getState().getScore()));
        speech.append(" ");
        speech.append(tellQuestionMessage);

        Map<String, Object> session = attributesManager.getSessionAttributes();
        session.put(GAME_KEY, game.getState());
        session.put("speechText", tellQuestionMessage);
        session.put("repromptText", tellQuestionMessage);
        session.put("cardTitleText", messages.getString("GAME_NAME"));
        attributesManager.setSessionAttributes(session);

        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", speech.toString());
        mv.put("reprompt", tellQuestionMessage);
        mv.put("card_title", messages.getString("GAME_NAME"));

        return mv;
    }

    private ModelAndView repeatFromSession(Map<String, Object> session) {
        ModelAndView mv = new ModelAndView("generic");
        mv.put("speech", session.get("speechText"));
        mv.put("reprompt", session.get("repromptText"));
        mv.put("card_title", session.get("cardTitleText"));

        return mv;
    }

    private ResourceBundle getMessages(Locale locale) {
        return ResourceBundle.getBundle(TriviaSkill.class.getPackage().getName() + ".responses.Messages", locale);
    }

    private Game getGame(Locale locale) {
        return new Game(locale, triviaDataProvider);
    }

    private String getTellQuestionMessage(ResourceBundle messages, Game game) {
        String answersText = getAnswersText(game);
        String message = String.format(messages.getString("TELL_QUESTION_MESSAGE"), game.getState().getCurrentQuestionIndex() + 1, game.getState().getCurrentQuestionText());
        return message + " " + answersText;
    }

    private String getAnswersText(Game game) {
        StringBuilder answersText = new StringBuilder();

        for(int i = 0; i < game.getState().getCurrentAnswers().size(); i++) {
            String currentAnswer = game.getState().getCurrentAnswers().get(i);
            answersText.append(i+1);
            answersText.append(". ");
            answersText.append(currentAnswer);
            answersText.append(". ");
        }

        return answersText.toString();
    }
}
