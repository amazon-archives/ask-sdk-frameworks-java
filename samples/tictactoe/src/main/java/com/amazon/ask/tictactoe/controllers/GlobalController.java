/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.tictactoe.controllers;

import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.tictactoe.Constants;

/**
 *
 */
public class GlobalController {

    @IntentMapping(type = StopIntent.class)
    public Response stop() {
        return getEndResponse();
    }

    @IntentMapping(type = CancelIntent.class)
    public Response cancel() {
        return getEndResponse();
    }

    private Response getEndResponse() {
        return Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText(Constants.END_RESPONSE)
                .build())
            .build();
    }

    @RequestMapping(SessionEndedRequest.class)
    public void sessionEnded(SessionEndedRequest request) {
        System.out.println(String.format("Session ended with reason %s", request.getReason()));
    }
}
