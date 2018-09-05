/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.argument;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.controller.MappingsController;
import com.amazon.ask.mvc.mapper.MethodParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IntentModelArgumentResolverTest {
    private IntentModelArgumentResolver resolver = new IntentModelArgumentResolver(IntentMapper.fromModel(MappingsController.buildPetSkillDefinition().getModel()));

    @Mock
    SkillContext mockSkillContext;

    @Test
    public void testSupportAndResolve() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getMethod("testSupportAndResolve"),
                0,
                MappingsController.PetTypeIntent.class,
                MethodParameter.EMPTY_ANNOTATIONS
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntent", "pet", "DRAGON");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        Object resolved = resolver.resolve(input).get();
        assertTrue(resolved instanceof MappingsController.PetTypeIntent);
    }

    @Test
    public void testDoesntSupport() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getMethod("testSupportAndResolve"),
                0,
                Map.class, //<---- wrong class
                MethodParameter.EMPTY_ANNOTATIONS
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntent", "pet", "DRAGON");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        Optional<Object> result = resolver.resolve(input);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSupportsParentClass() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getMethod("testSupportAndResolve"),
                0,
                Object.class, //<---- the param is of type "object" so the intent can still be passed
                MethodParameter.EMPTY_ANNOTATIONS
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntent", "pet", "DRAGON");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        assertTrue(resolver.resolve(input).isPresent());
    }
}