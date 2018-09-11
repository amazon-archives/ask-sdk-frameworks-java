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

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.mvc.MvcSkillApplication;

/**
 * Adapts the {@link ColorPickerSkill} for running within a Lambad environment.
 */
public class ColorPickerSkillLambda extends SkillStreamHandler {
    public ColorPickerSkillLambda() {
        this(new ColorPickerSkill());
    }

    public ColorPickerSkillLambda(MvcSkillApplication skillApplication) {
        super(skillApplication.getSkill());
    }
}
