/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data.model;


import com.amazon.ask.interaction.model.SlotTypeValue;
import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.interaction.data.SlotTypeDataSource;
import com.amazon.ask.interaction.data.source.Codec;
import com.amazon.ask.interaction.data.source.JsonCodec;
import com.amazon.ask.interaction.data.source.ResourceCandidateEnumerator;
import com.amazon.ask.interaction.data.source.ResourceSource;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;
import java.util.stream.Collectors;

import static com.amazon.ask.util.ValidationUtils.assertStringNotEmpty;

/**
 * Structure of a file containing a Slot Type's valuesIndex.
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

    private final Map<String, SlotValue> valuesIndex;

    public SlotTypeData(Map<String, SlotValue> valuesIndex) {
        this.valuesIndex = valuesIndex == null ? Collections.emptyMap() : Collections.unmodifiableMap(valuesIndex);
    }

    @JsonProperty("values")
    public Collection<SlotTypeValue> getValues() {
        return valuesIndex.entrySet().stream()
            .map(e -> SlotTypeValue.builder()
                .withId(e.getKey())
                .withName(e.getValue())
                .build())
            .collect(Collectors.toList());
    }

    @JsonIgnore
    public Map<String, SlotValue> getValuesIndex() {
        return valuesIndex;
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
        return Objects.equals(valuesIndex, that.valuesIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valuesIndex);
    }

    @Override
    public String toString() {
        return "SlotTypeData{" +
            "valuesIndex=" + valuesIndex +
            '}';
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
            return this.addValues(other.getValuesIndex());
        }

        @JsonProperty("values")
        public Builder withValues(Collection<SlotTypeValue> values) {
            this.values = values.stream()
                .peek(value -> assertStringNotEmpty(value.getId(), "slot value id"))
                .collect(Collectors.toMap(
                    SlotTypeValue::getId,
                    SlotTypeValue::getName));
            return this;
        }

        public Builder addValues(Collection<SlotTypeValue> values) {
            values.stream()
                .peek(value -> assertStringNotEmpty(value.getId(), "slot value id"))
                .forEach(v -> addValue(v.getId(), v.getName()));
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
