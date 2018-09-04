package com.amazon.ask.mvc;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.system.SystemState;

import java.util.Collections;
import java.util.Map;

public class Utils {
    public static final Response EMPTY_RESPONSE = Response.builder().build();

    public static HandlerInput buildSimpleSimpleIntentRequest(String intentName, String slotName, String slotValue) {
        return buildSimpleSimpleIntentRequest(intentName, slotName, slotValue, Collections.emptyMap());
    }

    public static HandlerInput buildSimpleSimpleIntentRequest(String intentName, String slotName, String slotValue, Map<String, Object> session) {
        return HandlerInput
                .builder()
                .withRequestEnvelope(buildSimpleEnvelope(intentName, slotName, slotValue, session))
                .build();
    }

    public static RequestEnvelope buildSimpleEnvelope(String intentName) {
        return buildSimpleEnvelope(intentName, "GREETING", "hola");
    }



    public static RequestEnvelope buildSimpleEnvelope(String intentName, String slotName, String slotValue) {
        return buildSimpleEnvelope(intentName, slotName, slotValue, Collections.emptyMap());
    }

    public static RequestEnvelope buildSimpleEnvelope(String intentName, String slotName, String slotValue, Map<String, Object> session) {
        Slot slot = Slot.builder()
            .withName(slotName)
            .withValue(slotValue).build();
        IntentRequest request = IntentRequest
                .builder()
                .withIntent(
                        Intent.builder()
                              .withName(intentName)
                              .withSlots(Collections.singletonMap(slotName, slot))
                              .build()
                )
                .withRequestId("rid")
                .build();

        return buildSimpleEnvelope(request, session);
    }

    public static RequestEnvelope buildSimpleEnvelope(Request request) {
        return buildSimpleEnvelope(request, Collections.emptyMap());
    }

    public static RequestEnvelope buildSimpleEnvelope(Request request, Map<String, Object> session) {
        return RequestEnvelope
                .builder()
                .withRequest(request)
                .withContext(
                        Context.builder()
                               .withSystem(
                                       SystemState.builder()
                                                  .withApplication(Application.builder().build())
                                                  .build()
                               )
                               .build()
                )
                .withSession(
                        Session.builder()
                               .withSessionId("sid")
                               .withAttributes(session)
                               .withApplication(Application.builder().build())
                               .build()
                )
                .build();
    }
}
