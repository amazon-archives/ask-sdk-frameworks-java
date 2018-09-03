package com.amazon.ask.models.data.model;


import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.models.data.SlotTypeDataSource;
import com.amazon.ask.models.data.source.Codec;
import com.amazon.ask.models.data.source.JsonCodec;
import com.amazon.ask.models.data.source.ResourceCandidateEnumerator;
import com.amazon.ask.models.data.source.ResourceSource;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

/**
 * Structure of a file containing a Slot Type's values.
 */
@JsonDeserialize(builder = SlotTypeData.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotTypeData {
    private static final SlotTypeData EMPTY = SlotTypeData.builder().build();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Codec<SlotTypeData> CODEC = new JsonCodec<>(MAPPER.readerFor(SlotTypeData.class));

    public static SlotTypeData combine(SlotTypeData a, SlotTypeData b) {
        return SlotTypeData.builder().add(a).add(b).build();
    }

    public static Codec<SlotTypeData> codec() {
        return CODEC;
    }

    public static SlotTypeData empty() {
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

    private final Map<String, SlotValue> values;

    public SlotTypeData(Map<String, SlotValue> values) {
        this.values = values == null ? Collections.emptyMap() : Collections.unmodifiableMap(values);
    }

    @JsonProperty("values")
    public Map<String, SlotValue> getValues() {
        return values;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotTypeData that = (SlotTypeData) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    public static class Resource extends ResourceSource<SlotTypeDefinition, SlotTypeData> implements SlotTypeDataSource {
        public Resource(Class resourceClass, Codec<SlotTypeData> codec, String name, String suffix, Set<ResourceCandidateEnumerator> resourceCandidateEnumerators) {
            super(resourceClass, codec, name, suffix, resourceCandidateEnumerators);
        }

        public static class Builder extends ResourceSource.Builder<Resource.Builder, SlotTypeDefinition, SlotTypeData> {
            public Resource build() {
                return new Resource(resourceClass, codec, name, suffix, resourceCandidateEnumerators);
            }
        }
    }

    public static class Builder {
        private Map<String, SlotValue> values;

        public SlotTypeData build() {
            return new SlotTypeData(values);
        }

        public Builder add(SlotTypeData other) {
            return this.addValues(other.getValues());
        }

        @JsonProperty("values")
        public Builder withValues(Map<String, SlotValue> values) {
            this.values = values;
            return this;
        }

        public Builder addValues(Map<String, SlotValue> values) {
            for (Map.Entry<String, SlotValue> entry: values.entrySet()) {
                this.addValue(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Builder addValue(String key, SlotValue value) {
            if (this.values == null) {
                this.values = new HashMap<>();
            }
            SlotValue merged = value;
            if (values.containsKey(key)) {
                SlotValue existing = values.get(key);

                if (!existing.getValue().equals(value.getValue())) {
                    // TODO: How do we apply here?
                    throw new IllegalArgumentException("Conflict add SlotValues: '" + existing.getValue() + "' and '" + value.getValue() + "'");
                }

                List<String> mergedSynonyms = new ArrayList<>(existing.getSynonyms());
                mergedSynonyms.addAll(value.getSynonyms());

                merged = SlotValue.builder()
                    .withValue(existing.getValue())
                    .withSynonyms(mergedSynonyms)
                    .build();
            }
            this.values.put(key, merged);
            return this;
        }
    }
}
