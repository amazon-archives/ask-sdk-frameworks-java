package com.amazon.ask.interaction.data.model;

import com.amazon.ask.interaction.model.DialogSlotPrompt;
import com.amazon.ask.interaction.model.PromptVariation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;
import java.util.function.Function;

import static com.amazon.ask.interaction.data.model.RequiredFlag.choose;

/**
 * Sub-structure of the {@link IntentData} file, containing a slot's samples and prompts.
 */
@JsonDeserialize(builder = IntentSlotData.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntentSlotData {
    private final Boolean confirmationRequired;
    private final Set<PromptVariation> confirmations;
    private final Boolean elicitationRequired;
    private final Set<PromptVariation> elicitations;
    private final DialogSlotPrompt prompts;
    private final Set<String> samples;

    public IntentSlotData(Boolean confirmationRequired, Set<PromptVariation> confirmations,
                          Boolean elicitationRequired, Set<PromptVariation> elicitations,
                          DialogSlotPrompt prompts,
                          Set<String> samples) {
        this.confirmationRequired = confirmationRequired;
        this.confirmations = confirmations == null ? Collections.emptySet() : Collections.unmodifiableSet(confirmations);
        this.elicitationRequired = elicitationRequired;
        this.elicitations = elicitations == null ? Collections.emptySet() : Collections.unmodifiableSet(elicitations);
        this.prompts = prompts;
        this.samples = samples == null ? Collections.emptySet() : Collections.unmodifiableSet(samples);
    }

    @JsonProperty("samples")
    public Set<String> getSamples() {
        return samples;
    }

    @JsonProperty("elicitations")
    public Set<PromptVariation> getElicitations() {
        return elicitations;
    }

    @JsonProperty("confirmations")
    public Set<PromptVariation> getConfirmations() {
        return confirmations;
    }

    @JsonProperty("confirmationRequired")
    public Boolean getConfirmationRequired() {
        return confirmationRequired;
    }

    @JsonProperty("elicitationRequired")
    public Boolean getElicitationRequired() {
        return elicitationRequired;
    }

    @JsonProperty("prompts")
    public DialogSlotPrompt getPrompts() {
        return prompts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntentSlotData that = (IntentSlotData) o;
        return Objects.equals(confirmationRequired, that.confirmationRequired) &&
            Objects.equals(confirmations, that.confirmations) &&
            Objects.equals(elicitationRequired, that.elicitationRequired) &&
            Objects.equals(elicitations, that.elicitations) &&
            Objects.equals(prompts, that.prompts) &&
            Objects.equals(samples, that.samples);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationRequired, confirmations, elicitationRequired, elicitations, prompts, samples);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Set<String> samples;
        private Set<PromptVariation> elicitations;
        private Set<PromptVariation> confirmations;
        private Boolean confirmationRequired;
        private Boolean elicitationRequired;
        private DialogSlotPrompt prompts;

        public Builder merge(IntentSlotData other) {
            return this
                .withConfirmationRequired(other.confirmationRequired)
                .addConfirmations(other.confirmations)
                .withElicitationRequired(other.elicitationRequired)
                .addElicitations(other.elicitations)
                .withPrompts(other.prompts)
                .addSamples(other.samples);
        }

        @JsonProperty("confirmations")
        public Builder withConfirmations(Collection<PromptVariation> confirmations) {
            this.confirmations = new LinkedHashSet<>(confirmations);
            return this;
        }

        public Builder addConfirmations(Collection<PromptVariation> confirmations) {
            confirmations.forEach(this::addConfirmation);
            return this;
        }

        public Builder addConfirmation(PromptVariation confirmation) {
            if (this.confirmations == null) {
                this.confirmations = new LinkedHashSet<>();
            }
            this.confirmations.add(confirmation);
            return this;
        }

        @JsonProperty("confirmationRequired")
        public Builder withConfirmationRequired(Boolean confirmationRequired) {
            this.confirmationRequired = choose(this.confirmationRequired, confirmationRequired);
            return this;
        }

        @JsonProperty("elicitations")
        public Builder withElicitations(Collection<PromptVariation> elicitations) {
            this.elicitations = new LinkedHashSet<>(elicitations);
            return this;
        }

        public Builder addElicitations(Collection<PromptVariation> elicitations) {
            elicitations.forEach(this::addElicitation);
            return this;
        }

        public Builder addElicitation(PromptVariation elicitation) {
            if (this.elicitations == null) {
                this.elicitations = new LinkedHashSet<>();
            }
            this.elicitations.add(elicitation);
            return this;
        }

        @JsonProperty("elicitationRequired")
        public Builder withElicitationRequired(Boolean elicitationRequired) {
            this.elicitationRequired = choose(this.elicitationRequired, elicitationRequired);
            return this;
        }

        @JsonProperty("prompts")
        public Builder withPrompts(DialogSlotPrompt prompts) {
            if (this.prompts == null) {
                this.prompts = prompts;
            } else if (prompts != null) {
                DialogSlotPrompt.Builder promptsBuilder = DialogSlotPrompt.builder();
                mergePrompt(this.prompts.getConfirmation(), prompts.getConfirmation(), promptsBuilder::withConfirmation);
                mergePrompt(this.prompts.getElicitation(), prompts.getElicitation(), promptsBuilder::withElicitation);
                this.prompts = promptsBuilder.build();
            }
            return this;
        }

        // Helper for merging confirmation/elicitation prompt ids
        private void mergePrompt(String thisPrompt, String otherPrompt, Function<String, DialogSlotPrompt.Builder> builder) {
            if (otherPrompt == null) {
                builder.apply(thisPrompt);
            } else if (thisPrompt == null || otherPrompt.equals(thisPrompt)) {
                builder.apply(otherPrompt);
            } else {
                throw new IllegalArgumentException("Conflicting prompt ids during add: " + thisPrompt + " and " + otherPrompt);
            }
        }

        @JsonProperty("samples")
        public Builder withSamples(Collection<String> samples) {
            this.samples = samples == null ? null : new LinkedHashSet<>(samples);
            return this;
        }

        public Builder addSamples(Collection<String> samples) {
            samples.forEach(this::addSample);
            return this;
        }

        public Builder addSample(String sample) {
            if (this.samples == null) {
                this.samples = new LinkedHashSet<>();
            }
            this.samples.add(sample);
            return this;
        }

        public IntentSlotData build() {
            return new IntentSlotData(confirmationRequired, confirmations, elicitationRequired, elicitations, prompts, samples);
        }
    }
}
