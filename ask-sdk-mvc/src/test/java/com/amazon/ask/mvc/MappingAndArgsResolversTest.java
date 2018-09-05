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

import com.amazon.ask.exception.UnhandledSkillException;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.events.skillevents.AccountLinkedRequest;
import com.amazon.ask.model.events.skillevents.PermissionAcceptedRequest;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import com.amazon.ask.mvc.controller.MappingsController;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MappingAndArgsResolversTest {
    private MappingsController controller = Mockito.spy(new MappingsController());

    private final MvcSkillApplication underTest = new MvcSkillApplication() {
        @Override
        protected List<SkillModule> getModules() {
            return Collections.singletonList(new SkillModule() {
                @Override
                public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
                    mvcBuilder.addController(controller);
                }

                @Override
                public void buildModel(Model.Builder modelBuilder) {
                    modelBuilder
                        .intent(MappingsController.PetTypeIntent.class)
                        .intent(MappingsController.PetTypeIntentTwo.class);
                }
            });
        }

        @Override
        protected Map<Locale, String> getInvocationNames() {
            return Collections.singletonMap(Locales.en_US, "test");
        }
    };

    /**
     * Test that magic param of type {@link RequestEnvelope} is resolved
     */
    @Test
    public void testWithEnvelopeParam() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Zero");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleEnvelope(envelope);
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that event mapping is routed to the right handler
     */
    @Test
    public void test_event_mapping() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope(AccountLinkedRequest.builder().build());
        underTest.getSkill().invoke(envelope);
        verify(controller).handleEvent();
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that event mapping is routed to the right handler when session attribute matches
     */
    @Test
    public void test_event_mapping_with_session_attribute() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope(PermissionAcceptedRequest.builder().build(), Collections.singletonMap(
            "food", "pizza"
        ));
        underTest.getSkill().invoke(envelope);
        verify(controller).handleEventWithSessionAttribute();
        verify(controller).requestInterceptor();
        verify(controller).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller).responseInterceptorWhenSesion();
    }

    /**
     * Verify that if a session attribute matches one of the values defined in {@link WhenSessionAttribute}
     * the method is invoked
     */
    @Test
    public void test_attribute_match() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Twelve", "meh", "meh", Collections.singletonMap(
            "food", "pizza" // <-- match
        ));

        underTest.getSkill().invoke(envelope);
        verify(controller).handleSessionAttr();
        verify(controller).requestInterceptor();
        verify(controller).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller).responseInterceptorWhenSesion();
    }

    /**
     * Verify that if a session attribute matches does not match one of the values defined in {@link WhenSessionAttribute}
     * the method is not invoked
     */
    @Test(expected = UnhandledSkillException.class)
    public void test_attribute_doesnt_match() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("SessionAttr", "meh", "meh", Collections.singletonMap(
            "food", "ABC"
        ));

        try {
            underTest.getSkill().invoke(envelope);
        } finally {
            verify(controller, never()).handleSessionAttr();
        }
    }

    /**
     * Test that a magic parameter which is a model for an intent is resolved
     */
    @Test
    public void testWithModelIntent() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntent", "pet", "DRAGON");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleModelIntent(notNull());
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that a magic parameter which is a model for a slot is resolved
     */
    @Test
    public void testWithModelSlot() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntentTwo", "pet", "DRAGON");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleModelSlot(MappingsController.PetType.DRAGON);
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of type {@link java.util.Map} with @SessionAttributes is resolved
     */
    @Test
    public void testWithSessionAttributesParam() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Ten");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleSession(envelope.getSession().getAttributes());
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of type {@link com.amazon.ask.model.Session} is resolved
     */
    @Test
    public void testWithSessionParam() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Nine");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleSession(envelope.getSession());
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of type {@link com.amazon.ask.attributes.AttributesManager} is resolved
     */
    @Test
    public void testWithAttributesManager() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Eleven");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleAttributesManager(notNull());
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of type Slot (with @Slot) is resolved
     */
    @Test
    public void testSlotParam() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Four");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleSlotArg(((IntentRequest) envelope.getRequest()).getIntent().getSlots().get("GREETING"));
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of type String (with @SlotValue) is resolved
     */
    @Test
    public void testSlotValueParam() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Five");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleSlotValue("hola");
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of type Map (with @SlotValues) is resolved
     */
    @Test
    public void testSlotValuesParam() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Six");
        underTest.getSkill().invoke(envelope);
        verify(controller).handleSlotValues(eq(Collections.singletonMap("GREETING", "hola")));
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test that magic param of all the supported are resolved
     */
    @Test
    public void testAllParamTypes() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Seven");
        underTest.getSkill().invoke(envelope);

        IntentRequest request = (IntentRequest) envelope.getRequest();
        Intent intent = request.getIntent();
        verify(controller).handleMultipleParams(
                same(envelope),
                same(request),
                same(intent),
                same(intent.getSlots().get("GREETING")),
                eq("hola"),
                eq(Collections.singletonMap("GREETING", "hola")));
        verify(controller).requestInterceptor();
        verify(controller, never()).requestInterceptorWhenSession();
        verify(controller).responseInterceptor();
        verify(controller, never()).responseInterceptorWhenSesion();
    }

    /**
     * Test an exception is thrown if the arg cannot be resolved
     */
    @Test(expected = UnhandledSkillException.class)
    public void testUnresolvedArgument() {
        RequestEnvelope envelope = Utils.buildSimpleEnvelope("Eight");
        underTest.getSkill().invoke(envelope);
        verify(controller).requestInterceptor();
        verify(controller, never()).responseInterceptor();
    }

    /**
     * Test that skill handles inherited Amazon default intents correctly
     */
    @Test
    public void testInheritedIntent() {
        underTest.getSkill().invoke(Utils.buildSimpleEnvelope("AMAZON.YesIntent", "", ""));

        verify(controller).handleCustomYes();
    }

    @Test
    public void testExceptionHandler() {
        ResponseEnvelope responseEnvelope = underTest.getSkill().invoke(Utils.buildSimpleEnvelope("IllegalArgumentException", "", ""));

        verify(controller).illegalArgumentException(any(IllegalArgumentException.class));
        assertEquals(Utils.EMPTY_RESPONSE, responseEnvelope.getResponse());
    }

    @Test
    public void testExceptionHandlerPredicate() {
        ResponseEnvelope responseEnvelope = underTest.getSkill().invoke(Utils.buildSimpleEnvelope("IllegalArgumentException", "", "", Collections.singletonMap(
            "food", "pizza" // <-- match
        )));

        verify(controller).throwIllegalArgumentException();
        verify(controller).illegalArgumentExceptionWhenSession(any(IllegalArgumentException.class));
        assertEquals(Utils.EMPTY_RESPONSE, responseEnvelope.getResponse());
    }
}
