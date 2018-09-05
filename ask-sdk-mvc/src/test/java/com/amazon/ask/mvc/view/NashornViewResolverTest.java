/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.mvc.view.nashorn.NashornViewResolver;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class NashornViewResolverTest {
    @Test
    public void testGlobalScript() throws Exception {
        NashornViewResolver resolver = NashornViewResolver.builder()
            .withResourceClass(getClass())
            .build();

        ModelAndView mav = new ModelAndView("global", Collections.singletonMap("attribute", "test"));
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("en-US")
                .build())
            .build();

        Response response = Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("hello test")
                .build())
            .build();

        View view = resolver.resolve(mav, requestEnvelope).get();

        assertEquals(response, view.render(mav, requestEnvelope));
    }

    @Test
    public void testInvokeFunction() throws Exception {
        NashornViewResolver resolver = NashornViewResolver.builder()
            .withResourceClass(getClass())
            .withRenderFunction("render")
            .build();

        ModelAndView mav = new ModelAndView("invoke_function", Collections.singletonMap("attribute", "test"));
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("en-US")
                .build())
            .build();

        Response response = Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("hello test")
                .build())
            .build();

        View view = resolver.resolve(mav, requestEnvelope).get();

        assertEquals(response, view.render(mav, requestEnvelope));
    }

    @Test
    public void testInvokeMethod() throws Exception {
        NashornViewResolver resolver = NashornViewResolver.builder()
            .withResourceClass(getClass())
            .withRenderObject("renderer", "render")
            .build();

        ModelAndView mav = new ModelAndView("invoke_method", Collections.singletonMap("attribute", "test"));
        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("en-US")
                .build())
            .build();

        Response response = Response.builder()
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("hello test")
                .build())
            .build();

        View view = resolver.resolve(mav, requestEnvelope).get();

        assertEquals(response, view.render(mav, requestEnvelope));
    }
}
