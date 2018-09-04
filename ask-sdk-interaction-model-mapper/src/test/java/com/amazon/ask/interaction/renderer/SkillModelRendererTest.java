package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.InteractionModel;
import com.amazon.ask.interaction.Locales;
import com.amazon.ask.interaction.definition.SkillModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SkillModelRendererTest {

    @Mock
    private InteractionModelRenderer mockRenderer;

    private InteractionModel mockInteractionModel = InteractionModel.builder().build();

    @Mock
    private SkillModel mockSkillModel;

    private SkillModelRenderer underTest;

    @Before
    public void before() {
        underTest = new SkillModelRenderer(mockRenderer);
    }

    @Test
    public void testRenderModel() {
        Mockito.when(mockRenderer.render(mockSkillModel, Locales.en_US)).thenReturn(mockInteractionModel);

        com.amazon.ask.interaction.model.SkillModel expected = com.amazon.ask.interaction.model.SkillModel.builder().withInteractionModel(mockInteractionModel).build();
        com.amazon.ask.interaction.model.SkillModel actual = underTest.render(mockSkillModel, Locales.en_US);
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testPropagateException() {
        Mockito.when(mockRenderer.render(mockSkillModel, Locales.en_US)).thenThrow(new RuntimeException());
        underTest.render(mockSkillModel, Locales.en_US);
    }
}
