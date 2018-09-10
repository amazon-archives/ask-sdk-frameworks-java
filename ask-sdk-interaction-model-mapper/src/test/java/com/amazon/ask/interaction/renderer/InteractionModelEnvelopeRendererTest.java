/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.InteractionModel;
import com.amazon.ask.interaction.Locales;
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.interaction.model.InteractionModelEnvelope;
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
public class InteractionModelEnvelopeRendererTest {

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

        InteractionModelEnvelope expected = InteractionModelEnvelope.builder().withInteractionModel(mockInteractionModel).build();
        InteractionModelEnvelope actual = underTest.render(mockSkillModel, Locales.en_US);
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testPropagateException() {
        Mockito.when(mockRenderer.render(mockSkillModel, Locales.en_US)).thenThrow(new RuntimeException());
        underTest.render(mockSkillModel, Locales.en_US);
    }
}
