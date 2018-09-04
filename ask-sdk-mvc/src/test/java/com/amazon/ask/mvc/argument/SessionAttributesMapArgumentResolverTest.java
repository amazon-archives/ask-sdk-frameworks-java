package com.amazon.ask.mvc.argument;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.controller.MappingsController;
import com.amazon.ask.mvc.mapper.MethodParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionAttributesMapArgumentResolverTest {
    private ArgumentResolver resolver = new SessionAttributesMapArgumentResolver();

    @Mock
    SkillContext mockSkillContext;

    @Test
    public void testSupportAndResolve() throws NoSuchMethodException {
        Method method = MappingsController.class.getMethod("handleSession", Map.class);
        MethodParameter methodParameter = new MethodParameter(
                method,
                0,
                Map.class,
                method.getParameterAnnotations()[0]
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        assertSame(envelope.getSession().getAttributes(), resolver.resolve(input).get());
    }

    @Test
    public void testDoesntSupport() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getMethod("testSupportAndResolve"),
                0,
                Object.class, //<---- wrong class
                MethodParameter.EMPTY_ANNOTATIONS
        );

        RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        assertFalse(resolver.resolve(input).isPresent());
    }
}