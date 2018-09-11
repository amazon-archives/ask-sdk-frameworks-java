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

import com.amazon.ask.builder.SkillBuilder;
import com.amazon.ask.colorpicker.handlers.exception.CatchAllExceptionHandler;
import com.amazon.ask.colorpicker.handlers.exception.UnhandledSkillExceptionHandler;
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.mvc.MvcSkillApplication;
import com.amazon.ask.mvc.SkillModule;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Entry point for a Lambda Skill, defines the MVC controllers and {@link SkillModel}
 */
public class ColorPickerSkill extends MvcSkillApplication {
    @Override
    protected Map<Locale, String> getInvocationNames() {
        return Collections.singletonMap(Locale.forLanguageTag("en-US"), "color picker");
    }

    @Override
    protected SkillBuilder getSkillBuilder() {
        return super.getSkillBuilder()
            .addExceptionHandler(new UnhandledSkillExceptionHandler())
            .addExceptionHandler(new CatchAllExceptionHandler());

    }

    @Override
    protected List<SkillModule> getModules() {
        return Collections.singletonList(new ColorPickerModule());
    }
}
