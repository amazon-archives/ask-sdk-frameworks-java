/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.annotation.plugin;

import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.renderer.RenderContext;

import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class RenderIntentContext {
    private final RenderContext<IntentDefinition> renderContext;
    private final SlotTypeDefinition slot;

    public RenderIntentContext(RenderContext<IntentDefinition> renderContext, SlotTypeDefinition slot) {
        this.renderContext = assertNotNull(renderContext, "renderContext");
        this.slot = slot;
    }

    public static Builder builder() {
        return new Builder();
    }

    public RenderContext<IntentDefinition> getRenderContext() {
        return renderContext;
    }

    public Optional<SlotTypeDefinition> getSlot() {
        return Optional.ofNullable(slot);
    }

    public static final class Builder {
        private RenderContext<IntentDefinition> renderContext;
        private SlotTypeDefinition slot;

        private Builder() {
        }

        public Builder withRenderContext(RenderContext<IntentDefinition> renderContext) {
            this.renderContext = renderContext;
            return this;
        }

        public Builder withSlot(SlotTypeDefinition slot) {
            this.slot = slot;
            return this;
        }

        public RenderIntentContext build() {
            return new RenderIntentContext(renderContext, slot);
        }
    }
}
