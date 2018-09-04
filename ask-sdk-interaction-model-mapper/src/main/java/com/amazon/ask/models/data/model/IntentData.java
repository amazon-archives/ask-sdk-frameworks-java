package com.amazon.ask.models.data.model;

import com.amazon.ask.interaction.model.DialogIntentPrompt;
import com.amazon.ask.interaction.model.PromptVariation;
import com.amazon.ask.models.data.IntentDataSource;
import com.amazon.ask.models.data.source.Codec;
import com.amazon.ask.models.data.source.JsonCodec;
import com.amazon.ask.models.data.source.ResourceCandidateEnumerator;
import com.amazon.ask.models.data.source.ResourceSource;
import com.amazon.ask.models.definition.IntentDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;
import java.util.function.Supplier;

import static com.amazon.ask.models.data.model.RequiredFlag.choose;

/**
 * Structure of a file containing an Intent's localized data:
 * - sample utterances
 * - slots and their data
 * - confirmation prompts
 * - confirmation required flag
 */
@JsonDeserialize(builder = IntentData.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntentData {
    private static final IntentData EMPTY = IntentData.builder().build();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Codec<IntentData> CODEC = new JsonCodec<>(MAPPER.readerFor(IntentData.class));

    public static IntentData combine(IntentData a, IntentData b) {
        return IntentData.builder().add(a).add(b).build();
    }

    /**
     * @return default json codec for {@link IntentData}
     */
    public static Codec<IntentData> codec() {
        return CODEC;
    }

    public static IntentData empty() {
        return EMPTY;
    }

    public static Resource.Builder resource() {
        return new Resource.Builder()
            .withCodec(codec())
            .withSuffix(".json");
    }

    public static Builder builder() {
        return new Builder();
    }

    private final Boolean confirmationRequired;
    private final Set<PromptVariation> confirmations;
    private final DialogIntentPrompt prompts;
    private final Map<String, IntentSlotData> slots;
    private final Set<String> samples;

    public IntentData(Boolean confirmationRequired,
                      Set<PromptVariation> confirmations,
                      DialogIntentPrompt prompts,
                      Set<String> samples,
                      Map<String, IntentSlotData> slots) {
        this.samples = samples == null ? Collections.emptySet() : Collections.unmodifiableSet(samples);
        this.confirmations = confirmations == null ? Collections.emptySet() : Collections.unmodifiableSet(confirmations);
        this.slots = slots == null ? Collections.emptyMap() : Collections.unmodifiableMap(slots);
        this.confirmationRequired = confirmationRequired;
        this.prompts = prompts;
    }

    @JsonProperty("samples")
    public Set<String> getSamples() {
        return samples;
    }

    @JsonProperty("confirmations")
    public Set<PromptVariation> getConfirmations() {
        return confirmations;
    }

    @JsonProperty("slots")
    public Map<String, IntentSlotData> getSlots() {
        return slots;
    }

    @JsonProperty("confirmationRequired")
    public Boolean getConfirmationRequired() {
        return confirmationRequired;
    }

    @JsonProperty("prompts")
    public DialogIntentPrompt getPrompts() {
        return prompts;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public String toString() {
        return "IntentData{" +
            "confirmationRequired=" + confirmationRequired +
            ", confirmations=" + confirmations +
            ", prompts=" + prompts +
            ", slots=" + slots +
            ", samples=" + samples +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntentData that = (IntentData) o;
        return Objects.equals(confirmationRequired, that.confirmationRequired) &&
            Objects.equals(confirmations, that.confirmations) &&
            Objects.equals(prompts, that.prompts) &&
            Objects.equals(slots, that.slots) &&
            Objects.equals(samples, that.samples);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationRequired, confirmations, prompts, slots, samples);
    }

    public static class Resource extends ResourceSource<IntentDefinition, IntentData> implements IntentDataSource {
        public Resource(Class resourceClass, Codec<IntentData> codec, String name, String suffix, Set<ResourceCandidateEnumerator> resourceCandidateEnumerators) {
            super(resourceClass, codec, name, suffix, resourceCandidateEnumerators);
        }

        public static class Builder extends ResourceSource.Builder<Builder, IntentDefinition, IntentData> {
            public Resource build() {
                return new Resource(resourceClass, codec, name, suffix, resourceCandidateEnumerators);
            }
        }
    }

    public static class Builder {
        private Boolean confirmationRequired;
        private Set<PromptVariation> confirmations;
        private DialogIntentPrompt prompts;
        private Set<String> samples;
        private Map<String, IntentSlotData> slots;

        public IntentData build() {
            return new IntentData(confirmationRequired, confirmations, prompts, samples, slots);
        }

        public Builder add(IntentData other) {
            return this
                .withConfirmationRequired(other.confirmationRequired)
                .withPrompts(other.prompts)
                .addConfirmations(other.confirmations)
                .addSamples(other.samples)
                .addSlots(other.slots);
        }

        @JsonProperty("confirmationRequired")
        public Builder withConfirmationRequired(Boolean confirmationRequired) {
            this.confirmationRequired = choose(this.confirmationRequired, confirmationRequired);
            return this;
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

        @JsonProperty("prompts")
        public Builder withPrompts(DialogIntentPrompt prompts) {
            if (this.prompts == null) {
                this.prompts = prompts;
            } else if (prompts != null && !this.prompts.equals(prompts)) {
                throw new IllegalArgumentException("can not add conflicting confirmation prompt ids: " + this.prompts + " and " + prompts);
            }
            return this;
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

        @JsonProperty("slots")
        public Builder withSlots(Map<String, IntentSlotData> slots) {
            this.slots = slots;
            return this;
        }

        public Builder addSlots(Map<String, IntentSlotData> slots) {
            for (Map.Entry<String, IntentSlotData> entry: slots.entrySet()) {
                this.addSlot(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Builder addSlot(String name, IntentSlotData slot) {
            if (this.slots == null) {
                this.slots = new HashMap<>();
            }
            IntentSlotData merged = slot;
            if (slots.containsKey(name)) {
                merged = IntentSlotData.builder()
                    .merge(slots.get(name))
                    .merge(slot)
                    .build();
            }
            this.slots.put(name, merged);
            return this;
        }
    }
}
