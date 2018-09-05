/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data;

import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.renderer.RenderContext;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 */
public interface SlotTypeDataResolver extends Function<RenderContext<SlotTypeDefinition>, Stream<SlotTypeData>> {
}
