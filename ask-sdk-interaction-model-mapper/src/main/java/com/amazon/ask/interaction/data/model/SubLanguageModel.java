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

import com.amazon.ask.interaction.model.Intent;
import com.amazon.ask.interaction.model.LanguageModel;
import com.amazon.ask.interaction.model.SlotType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Fork of the {@link LanguageModel}, removing invocation name.
 */
@JsonDeserialize(builder = SubLanguageModel.Builder.class)
public class SubLanguageModel {
    private final Collection<Intent> intents;
    private final Collection<SlotType> types;

    protected SubLanguageModel(List<Intent> intents, List<SlotType> types) {
        this.intents = intents == null ? emptyList() : unmodifiableList(intents);
        this.types = types == null ? emptyList() : unmodifiableList(types);
    }

    @JsonProperty("intents")
    public Collection<Intent> getIntents() {
        return intents;
    }

    @JsonProperty("types")
    public Collection<SlotType> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubLanguageModel that = (SubLanguageModel) o;
        return Objects.equals(intents, that.intents) && Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intents, types);
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        protected List<Intent> intents;
        protected List<SlotType> types;

        private Builder() {}

        @JsonProperty("intents")
        public Builder withIntents(List<Intent> intents) {
            this.intents = intents;
            return this;
        }

        public Builder addIntents(List<Intent> intents) {
            intents.forEach(this::addIntent);
            return this;
        }

        public Builder addIntent(Intent intent) {
            if (this.intents == null) {
                this.intents = new ArrayList<>();
            }
            this.intents.add(intent);
            return this;
        }

        @JsonProperty("types")
        public Builder withTypes(List<SlotType> types) {
            this.types = types;
            return this;
        }

        public Builder addTypes(List<SlotType> types) {
            types.forEach(this::addType);
            return this;
        }

        public Builder addType(SlotType type) {
            if (this.types == null) {
                this.types = new ArrayList<>();
            }
            this.types.add(type);
            return this;
        }

        public SubLanguageModel build() {
            return new SubLanguageModel(intents, types);
        }
    }
}
