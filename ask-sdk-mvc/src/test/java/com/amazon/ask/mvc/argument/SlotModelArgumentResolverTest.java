package com.amazon.ask.mvc.argument;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.controller.MappingsController;
import com.amazon.ask.mvc.mapper.MethodParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SlotModelArgumentResolverTest {
    private ArgumentResolver resolver = new SlotModelArgumentResolver(IntentMapper.fromModel(MappingsController.buildPetSkillDefinition().getModel()));

    @Mock
    SkillContext mockSkillContext;

    @Test
    public void testSupportAndResolve() throws NoSuchMethodException {
        Method method = MappingsController.class.getMethod("handleModelSlot", MappingsController.PetType.class);
        MethodParameter methodParameter = new MethodParameter(
                method,
                0,
                MappingsController.PetType.class,
                method.getParameterAnnotations()[0]
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntentTwo", "pet", "DRAGON");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        assertEquals(MappingsController.PetType.DRAGON, resolver.resolve(input).get());
    }

    @Test
    public void testDoesntSupport() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getMethod("testSupportAndResolve"),
                0,
                Map.class, //<---- wrong class
                MethodParameter.EMPTY_ANNOTATIONS
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("PetTypeIntent2", "pet", "DRAGON");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        assertFalse(resolver.resolve(input).isPresent());
    }
}
