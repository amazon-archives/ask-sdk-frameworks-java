package com.amazon.ask.mvc.argument;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.util.Locale;

import com.amazon.ask.mvc.mapper.MethodParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocaleArgumentResolverTest {
    private LocaleArgumentResolver resolver = new LocaleArgumentResolver();

    @Mock
    SkillContext mockSkillContext;

    @Test
    public void testSupportAndResolve() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
            this.getClass().getMethod("testSupportAndResolve"),
            0,
            Locale.class,
            MethodParameter.EMPTY_ANNOTATIONS
        );

        RequestEnvelope envelope = RequestEnvelope.builder()
            .withRequest(
                LaunchRequest.builder()
                .withLocale("ja-JP")
                .build()
            )
            .build();

        ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

        assertEquals(Locale.JAPAN, resolver.resolve(input).get());
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
