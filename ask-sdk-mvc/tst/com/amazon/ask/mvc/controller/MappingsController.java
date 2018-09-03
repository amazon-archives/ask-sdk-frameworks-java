package com.amazon.ask.mvc.controller;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.model.events.skillevents.AccountLinkedRequest;
import com.amazon.ask.model.events.skillevents.PermissionAcceptedRequest;
import com.amazon.ask.models.annotation.data.SlotProperty;
import com.amazon.ask.models.annotation.type.SlotType;
import com.amazon.ask.models.definition.Model;
import com.amazon.ask.models.definition.SkillModel;
import com.amazon.ask.models.annotation.data.IntentResource;
import com.amazon.ask.models.annotation.type.Intent;
import com.amazon.ask.models.types.intent.YesIntent;
import com.amazon.ask.mvc.Locales;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.annotation.argument.SessionAttributes;
import com.amazon.ask.mvc.annotation.argument.SlotValues;
import com.amazon.ask.mvc.mapper.Priority;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.annotation.mapping.ExceptionHandler;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestInterceptor;
import com.amazon.ask.mvc.annotation.mapping.ResponseInterceptor;

import java.util.*;

public class MappingsController extends BaseMappingsController {
    @RequestInterceptor
    public void requestInterceptor() {
    }

    @RequestInterceptor
    @WhenSessionAttribute(path = "food", hasValues = {"sushi", "pizza"})
    public void requestInterceptorWhenSession() {
    }

    @ResponseInterceptor
    public void responseInterceptor() {

    }

    @ResponseInterceptor
    @WhenSessionAttribute(path = "food", hasValues = {"sushi", "pizza"})
    public void responseInterceptorWhenSesion() {

    }

    @IntentMapping(name = "IllegalArgumentException")
    public Response throwIllegalArgumentException() {
        throw new IllegalArgumentException("throwIllegalArgumentException");
    }

    @ExceptionHandler(exception = IllegalArgumentException.class)
    public Response illegalArgumentException(Exception ex) {
        return Utils.EMPTY_RESPONSE;
    }

    @Priority(1)// make sure it's checked prior to #illegalArgumentException
    @ExceptionHandler(exception = IllegalArgumentException.class)
    @WhenSessionAttribute(path = "food", hasValues = {"sushi", "pizza"})
    public Response illegalArgumentExceptionWhenSession(Exception ex) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Zero")
    public Response handleEnvelope(RequestEnvelope envelope) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "One")
    public Response handleRequest(IntentRequest request) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Two")
    public Response handleRequest(com.amazon.ask.model.Intent intent) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Three")
    public Response handleNoArgs() {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Four")
    public Response handleSlotArg(@com.amazon.ask.mvc.annotation.argument.Slot("GREETING") com.amazon.ask.model.Slot slot) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Five")
    public Response handleSlotValue(@com.amazon.ask.mvc.annotation.argument.Slot("GREETING") String value) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Six")
    public Response handleSlotValues(@SlotValues Map<String, String> values) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Seven")
    public Response handleMultipleParams(
        RequestEnvelope envelope,
        IntentRequest request,
        com.amazon.ask.model.Intent intent,
        @com.amazon.ask.mvc.annotation.argument.Slot("GREETING") com.amazon.ask.model.Slot slot,
        @com.amazon.ask.mvc.annotation.argument.Slot("GREETING") String slotValue,
        @SlotValues Map<String, String> values
    ) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Eight")
    public Response nonResolveParam(Object obj) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Nine")
    public Response handleSession(Session session) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Ten")
    public Response handleSession(@SessionAttributes Map attributes) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(type = PetTypeIntent.class)
    public Response handleModelIntent(PetTypeIntent intent) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(type = PetTypeIntentTwo.class)
    public Response handleModelSlot(@com.amazon.ask.mvc.annotation.argument.Slot("pet") PetType petType) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Eleven")
    public Response handleAttributesManager(AttributesManager attributesManager) {
        return Utils.EMPTY_RESPONSE;
    }

    @IntentMapping(name = "Twelve")
    @WhenSessionAttribute(path = "food", hasValues = {"sushi", "pizza"}, matchNull = true)
    public Response handleSessionAttr() {return Utils.EMPTY_RESPONSE;}

    @RequestMapping({SessionEndedRequest.class, AccountLinkedRequest.class})
    public Response handleEvent() {return Utils.EMPTY_RESPONSE;}

    @RequestMapping({PermissionAcceptedRequest.class})
    @WhenSessionAttribute(path = "food", hasValues = {"sushi", "pizza"})
    public Response handleEventWithSessionAttribute() {return Utils.EMPTY_RESPONSE;}

    @IntentMapping(type = CustomYesIntent.class)
    public Response handleCustomYes() {return Utils.EMPTY_RESPONSE;}

    @SlotType("PetType")
    @IntentResource("PetType.json")
    public enum PetType {
        CAT,
        DOG,
        DRAGON;
    }

    @Intent("PetTypeIntent")
    @IntentResource("PetTypeIntent.json")
    public static class PetTypeIntent {
        @SlotProperty
        private PetType pet;

        public PetType getPet() {
            return pet;
        }

        public void setPet(PetType pet) {
            this.pet = pet;
        }
    }

    @Intent // unit test auto intent name scheme
    @IntentResource("PetTypeIntentTwo.json")
    public static class PetTypeIntentTwo {
        @SlotProperty
        private PetType pet;

        public PetType getPet() {
            return pet;
        }

        public void setPet(PetType pet) {
            this.pet = pet;
        }
    }

    @IntentResource("CustomYesIntent.json")
    public static class CustomYesIntent extends YesIntent {
    }

    public static SkillModel buildPetSkillDefinition() {
        return SkillModel.builder()
            .withInvocationName(Locales.en_US, "en_US")
            .addModel(Model.builder()
                .intent(PetTypeIntent.class)
                .intent(PetTypeIntentTwo.class)
                .build())
            .build();
    }
}
