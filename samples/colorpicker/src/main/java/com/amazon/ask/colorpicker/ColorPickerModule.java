/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.colorpicker;

import com.amazon.ask.colorpicker.controllers.ColorPickerController;
import com.amazon.ask.colorpicker.intents.MyColorIsIntent;
import com.amazon.ask.colorpicker.intents.WhatsMyColorIntent;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.FreeMarkerViewResolver;

/**
 *
 */
public class ColorPickerModule implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        mvcBuilder
            .addController(new ColorPickerController())
            .addViewResolvers(FreeMarkerViewResolver.builder()
                .withResourceClass(ColorPickerSkill.class)
                .withPrefix("views/")
                .build());
    }

    @Override
    public void buildModel(Model.Builder modelBuilder) {
        modelBuilder
            .intent(CancelIntent.class)
            .intent(HelpIntent.class)
            .intent(MyColorIsIntent.class)
            .intent(StopIntent.class)
            .intent(WhatsMyColorIntent.class);
    }
}
