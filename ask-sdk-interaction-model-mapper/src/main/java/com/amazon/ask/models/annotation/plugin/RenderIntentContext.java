package com.amazon.ask.models.annotation.plugin;

import com.amazon.ask.models.definition.IntentDefinition;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.amazon.ask.models.renderer.RenderContext;

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
