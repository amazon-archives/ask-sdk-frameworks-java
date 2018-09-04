package com.amazon.ask.decisiontree;

import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.models.types.intent.CancelIntent;
import com.amazon.ask.models.types.intent.HelpIntent;
import com.amazon.ask.models.types.intent.StopIntent;
import com.amazon.ask.models.types.slot.BaseSlotValue;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.annotation.condition.WhenDialogState;
import com.amazon.ask.mvc.view.ModelAndView;
import com.amazon.ask.decisiontree.intents.CouchPotatoIntent;
import com.amazon.ask.decisiontree.intents.RecommendationIntent;

import com.amazon.ask.decisiontree.slots.SalaryImportanceTypeSlot;
import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeController {
    private final List<String> requiredSlots = Arrays.asList("preferredSpecies", "bloodTolerance", "personality", "salaryImportance");

    @RequestMapping(LaunchRequest.class)
    public ModelAndView launch() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Welcome to Decision Tree. I will recommend the best job for you. Do you want to start your career or be a couch potato?");
        mv.put("reprompt", "Do you want a career or to be a couch potato?");

        return mv;
    }

    @IntentMapping(type = HelpIntent.class)
    public ModelAndView help() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "This is Decision Tree. I can help you find the perfect job. You can say, recommend a job.");
        mv.put("reprompt", "Would you like to start a career or do you want to be a couch potato?");

        return mv;
    }

    @IntentMapping(type = StopIntent.class)
    public ModelAndView stop() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Bye");

        return mv;
    }

    @IntentMapping(type = CancelIntent.class)
    public ModelAndView cancel() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Bye");

        return mv;
    }

    @RequestMapping(SessionEndedRequest.class)
    public void sessionEnded(SessionEndedRequest request) {
        System.out.println(String.format("Session ended with reason %s", request.getReason()));
    }

    @IntentMapping(type = CouchPotatoIntent.class)
    public ModelAndView couchPotatoIntent() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "You don't want to start your career? Have fun wasting away on the couch.");
        mv.put("reprompt", "Would you like to start a career or do you want to be a couch potato?");

        return mv;
    }

    @IntentMapping(type = RecommendationIntent.class)
    @WhenDialogState({DialogState.IN_PROGRESS, DialogState.STARTED})
    public ModelAndView inProgressRecommendationIntent(Intent intent) {
        Map<String, Slot> slots = intent.getSlots();

        Set<Slot> unconfirmedSlots = slots.values().stream()
            .filter(slot ->
                slot.getConfirmationStatus() != SlotConfirmationStatus.CONFIRMED &&
                    slot.getResolutions() != null && slot.getResolutions().getResolutionsPerAuthority().size() > 0
            )
            .collect(Collectors.toSet());

        for (Slot slot : unconfirmedSlots) {
            Resolution currentResolution = slot.getResolutions().getResolutionsPerAuthority().get(0);
            if (currentResolution.getStatus().getCode() == StatusCode.ER_SUCCESS_MATCH) {
                if (currentResolution.getValues().size() > 1) {
                    List<String> slotValueNames = currentResolution.getValues().stream().map(x -> x.getValue().getName()).collect(Collectors.toList());

                    String prompt = "Which would you like: ";
                    prompt += String.join(" or ", slotValueNames);
                    prompt += "?";

                    return confirmSlot(prompt, slot.getName());
                }
            } else if (currentResolution.getStatus().getCode() == StatusCode.ER_SUCCESS_NO_MATCH) {
                if (requiredSlots.contains(slot.getName())) {
                    String prompt = "What " + slot.getName() + " are you looking for";

                    return confirmSlot(prompt, slot.getName());
                }
            }
        }

        return new ModelAndView("delegateIntent", Collections.singletonMap("currentIntent", intent));
    }

    @IntentMapping(type = RecommendationIntent.class)
    @WhenDialogState(DialogState.COMPLETED)
    public ModelAndView completedRecommendationIntent(RecommendationIntent intent) {
        RecommendationService service = new RecommendationService();
        String recommendation = service.getRecommendation(
            getSlotFirstResolutionValue(intent.getSalaryImportance()),
            getSlotFirstResolutionValue(intent.getPersonality()),
            getSlotFirstResolutionValue(intent.getBloodTolerance()),
            getSlotFirstResolutionValue(intent.getPreferredSpecies())
        );

        String salaryText = getSalaryImportanceText(intent.getSalaryImportance());

        ModelAndView mv = new ModelAndView("completeRecommendation");
        mv.put("currentIntent", intent);
        mv.put("recommendation", recommendation);
        mv.put("salary", salaryText);

        return mv;
    }

    private ModelAndView confirmSlot(String prompt, String slotName) {
        ModelAndView mv = new ModelAndView("slotConfirmation");
        mv.put("prompt", prompt);
        mv.put("slotToElicit", slotName);
        return mv;
    }

    private String getSlotFirstResolutionValue(BaseSlotValue slot) {
        return slot.getSlot().getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0)
            .getValue().getName();
    }

    private String getSalaryImportanceText(SalaryImportanceTypeSlot salaryImportance) {
        Optional<Resolution> resolvedValue = salaryImportance.getSlot().getResolutions().getResolutionsPerAuthority().stream()
            .filter(x -> x.getStatus().getCode().equals(StatusCode.ER_SUCCESS_MATCH))
            .filter(x -> x.getValues().size() > 0)
            .findFirst();

        if (!resolvedValue.isPresent()) {
            throw new IllegalArgumentException("Salary importance not present");
        }

        String importanceValue = resolvedValue.get().getValues().get(0).getValue().getName();

        if (importanceValue.equals("unimportant")) {
            return "not important at all";
        }

        if (importanceValue.equals("somewhat")) {
            return "not your top priority";
        }

        return importanceValue + " important to you";
    }
}
