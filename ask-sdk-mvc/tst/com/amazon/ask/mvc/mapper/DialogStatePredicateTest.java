package com.amazon.ask.mvc.mapper;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.annotation.condition.WhenDialogState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DialogStatePredicateTest {
    private WhenDialogState.Plugin underTest = new WhenDialogState.Plugin();

    private Predicate<HandlerInput> inProgress;
    private Predicate<HandlerInput> inProgressOrCompleted;

    @Mock
    SkillContext mockSkillContext;

    @Before
    public void before() throws NoSuchMethodException {
        this.inProgress = underTest
            .apply(
                ControllerMethodContext.builder()
                    .withSkillContext(mockSkillContext)
                    .withController(this)
                    .withMethod(this.getClass().getMethod("inProgress"))
                    .build(),
                this.getClass().getMethod("inProgress").getAnnotation(WhenDialogState.class));

        this.inProgressOrCompleted = underTest
            .apply(
                ControllerMethodContext.builder()
                    .withSkillContext(mockSkillContext)
                    .withController(this)
                    .withMethod(this.getClass().getMethod("inProgressOrCompleted"))
                    .build(),
                this.getClass().getMethod("inProgressOrCompleted").getAnnotation(WhenDialogState.class));
    }

    @Test
    public void testNotIntentRequest() {
        HandlerInput handlerInput = HandlerInput.builder()
            .withRequestEnvelope(RequestEnvelope.builder()
                .withRequest(LaunchRequest.builder()
                    .build())
                .build())
            .build();

        assertFalse(inProgress.test(handlerInput));
        assertFalse(inProgressOrCompleted.test(handlerInput));
    }

    @Test
    public void testNullDialog() {
        assertFalse(inProgress.test(testCase(null)));
        assertFalse(inProgressOrCompleted.test(testCase(null)));
    }

    @Test
    public void testConditions() {
        assertFalse(inProgress.test(testCase(DialogState.COMPLETED)));
        assertTrue(inProgress.test(testCase(DialogState.IN_PROGRESS)));
        assertFalse(inProgress.test(testCase(DialogState.STARTED)));


        assertTrue(inProgressOrCompleted.test(testCase(DialogState.COMPLETED)));
        assertTrue(inProgressOrCompleted.test(testCase(DialogState.IN_PROGRESS)));
        assertFalse(inProgressOrCompleted.test(testCase(DialogState.STARTED)));
    }

    private HandlerInput testCase(DialogState state) {
        return HandlerInput.builder()
            .withRequestEnvelope(RequestEnvelope.builder()
                .withRequest(IntentRequest.builder()
                    .withDialogState(state)
                    .build())
                .build())
            .build();
    }

    @WhenDialogState(DialogState.IN_PROGRESS)
    public void inProgress() {
    }

    @WhenDialogState({DialogState.COMPLETED, DialogState.IN_PROGRESS})
    public void inProgressOrCompleted() {
    }
}
