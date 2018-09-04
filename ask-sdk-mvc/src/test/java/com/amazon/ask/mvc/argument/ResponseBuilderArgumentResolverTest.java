package com.amazon.ask.mvc.argument;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.mapper.MethodParameter;
import com.amazon.ask.response.ResponseBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResponseBuilderArgumentResolverTest {
  private ArgumentResolver resolver = new ResponseBuilderArgumentResolver();

  @Mock
  SkillContext mockSkillContext;

  @Test
  public void testSupportAndResolve() throws NoSuchMethodException {
    MethodParameter methodParameter = new MethodParameter(
        this.getClass().getMethod("testSupportAndResolve"),
        0,
        ResponseBuilder.class,
        MethodParameter.EMPTY_ANNOTATIONS
    );

    RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
    HandlerInput handlerInput =  HandlerInput.builder().withRequestEnvelope(envelope).build();
    ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, handlerInput);

    assertTrue(resolver.resolve(input).get() instanceof ResponseBuilder);
  }

  @Test
  public void testResolvesNewInstance() throws NoSuchMethodException {
    MethodParameter methodParameter = new MethodParameter(
        this.getClass().getMethod("testSupportAndResolve"),
        0,
        ResponseBuilder.class,
        MethodParameter.EMPTY_ANNOTATIONS
    );

    RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
    HandlerInput handlerInput =  HandlerInput.builder().withRequestEnvelope(envelope).build();
    ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, handlerInput);

    Object resolved = resolver.resolve(input);
    Object secondResolved = resolver.resolve(input);

    assertNotSame(handlerInput.getResponseBuilder(), resolved);
    assertNotSame(secondResolved, resolved);
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
