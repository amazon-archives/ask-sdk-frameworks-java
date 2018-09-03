package com.amazon.ask.mvc.mapper;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WhenSessionAttributeTest {
    private final WhenSessionAttribute.Plugin underTest = new WhenSessionAttribute.Plugin();

    @Mock
    SkillContext mockSkillContext;

    @Test
    public void test_value_in_session_matches() throws NoSuchMethodException {
        Predicate<HandlerInput> predicate = buildPredicate("inSession");

        HandlerInput dispatcherInput = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh", Collections.singletonMap(
            "attr", "B"
        ));

        assertTrue(predicate.test(dispatcherInput));
    }

    @Test
    public void test_value_in_session_doesnt_matches() throws NoSuchMethodException {
        Predicate<HandlerInput> predicate = buildPredicate("inSession");
        HandlerInput dispatcherInput = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh", Collections.singletonMap(
            "attr", "XYZ"
        ));

        assertFalse(predicate.test(dispatcherInput));
    }

    @Test
    public void test_value_not_in_session() throws NoSuchMethodException {
        Predicate<HandlerInput> predicate = buildPredicate("inSession");
        HandlerInput dispatcherInput = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh");

        assertFalse(predicate.test(dispatcherInput));
    }

    @Test
    public void test_value_not_in_session_match_undefined() throws NoSuchMethodException {
        Predicate<HandlerInput> predicate = buildPredicate("inSessionUndef");
        HandlerInput dispatcherInput = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh");

        assertTrue(predicate.test(dispatcherInput));
    }

    @Test
    public void test_nested_value_in_session() throws Exception {
        Predicate<HandlerInput> predicate = buildPredicate("inSessionNested");

        HandlerInput dispatcherInput = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh", Collections.singletonMap(
            "a", Collections.singletonMap("b", "A")
        ));

        assertTrue(predicate.test(dispatcherInput));
    }

    @Test
    public void test_nested_value_doesnt_match() throws Exception {
        Predicate<HandlerInput> predicate = buildPredicate("inSessionNested");

        HandlerInput rootDoesntExist = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh");
        HandlerInput valueDoesntMatch = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh", Collections.singletonMap(
            "a", Collections.singletonMap("b", "INVALID")
        ));
        HandlerInput notNested = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh", Collections.singletonMap(
            "a", "B"
        ));
        HandlerInput keyDoesntExist = Utils.buildSimpleSimpleIntentRequest("whatever", "meh", "meh", Collections.singletonMap(
            "a", Collections.singletonMap("c", "value")
        ));

        assertFalse(predicate.test(rootDoesntExist));
        assertFalse(predicate.test(valueDoesntMatch));
        assertFalse(predicate.test(notNested));
        assertFalse(predicate.test(keyDoesntExist));
    }

    private Predicate<HandlerInput> buildPredicate(String methodName) throws NoSuchMethodException {
        return underTest
            .apply(
                ControllerMethodContext.builder()
                    .withSkillContext(mockSkillContext)
                    .withController(this)
                    .withMethod(getClass().getMethod(methodName))
                    .build(),
                getClass().getMethod(methodName).getAnnotation(WhenSessionAttribute.class));
    }

    @WhenSessionAttribute(path = "attr", hasValues = {"A", "B"})
    public void inSession() {

    }

    @WhenSessionAttribute(path = "attr", hasValues = {"A", "B"}, matchNull = true)
    public void inSessionUndef() {

    }

    @WhenSessionAttribute(path = {"a", "b"}, hasValues = "A")
    public void inSessionNested() {

    }
}
