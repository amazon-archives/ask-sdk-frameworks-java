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