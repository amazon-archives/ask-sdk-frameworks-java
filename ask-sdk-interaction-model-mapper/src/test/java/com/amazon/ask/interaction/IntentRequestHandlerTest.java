/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.interaction.types.slot.date.AmazonDate;
import com.amazon.ask.interaction.types.slot.date.MonthDate;
import com.amazon.ask.interaction.types.slot.date.SpecificDate;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class IntentRequestHandlerTest {

    @Mock
    IntentRequestHandler<TestIntent> spy;

    @Mock
    HandlerInput mockInput;

    IntentMapper mapper = IntentMapper.fromModel(Model.builder()
        .intent(TestIntent.class)
        .build());

    @Test
    public void testCanHandleFalseIfMismatch() {
        TestIntentHandler underTest = new TestIntentHandler(mapper);

        when(mockInput.getRequestEnvelope()).thenReturn(RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withIntent(com.amazon.ask.model.Intent.builder()
                    .withName("NoMatch")
                    .withSlots(Collections.emptyMap())
                    .build())
                .build())
            .build());

        assertFalse(underTest.canHandle(mockInput));
    }

    @Test
    public void testCanHandleNameTrueIfMatches() {
        TestIntentHandler underTest = new TestIntentHandler(mapper);

        when(mockInput.getRequestEnvelope()).thenReturn(RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withIntent(com.amazon.ask.model.Intent.builder()
                    .withName("TestIntent")
                    .withSlots(Collections.emptyMap())
                    .build())
                .build())
            .build());

        assertTrue(underTest.canHandle(mockInput));
    }

    @Test
    public void testParseInHandle() {
        TestIntentHandler underTest = new TestIntentHandler(mapper);

        when(mockInput.getRequestEnvelope()).thenReturn(RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withIntent(com.amazon.ask.model.Intent.builder()
                    .withName("TestIntent")
                    .withSlots(Collections.singletonMap(
                        "amazonDate", Slot.builder()
                            .withName("amazonDate")
                            .withValue("2018-12-01")
                            .build()
                    ))
                    .build())
                .build())
            .build());

        underTest.handle(mockInput);

        TestIntent expected = new TestIntent();
        expected.setAmazonDate(new SpecificDate(Slot.builder()
            .withName("amazonDate")
            .withValue("2018-12-01")
            .build(), LocalDate.of(2018, 12, 1)));

        verify(spy).handle(mockInput, expected);
    }


    @Intent
    public static class TestIntent {
        @SlotProperty
        private AmazonDate amazonDate;

        public AmazonDate getAmazonDate() {
            return amazonDate;
        }

        public void setAmazonDate(AmazonDate amazonDate) {
            this.amazonDate = amazonDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestIntent that = (TestIntent) o;
            return Objects.equals(amazonDate, that.amazonDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(amazonDate);
        }
    }

    public class TestIntentHandler extends IntentRequestHandler<TestIntent> {
        public TestIntentHandler(IntentMapper intentMapper) {
            super(TestIntent.class, intentMapper);
        }

        @Override
        protected Optional<Response> handle(HandlerInput input, TestIntent intent) {
            return spy.handle(input, intent);
        }
    }

}
