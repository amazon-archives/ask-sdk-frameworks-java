/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

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
