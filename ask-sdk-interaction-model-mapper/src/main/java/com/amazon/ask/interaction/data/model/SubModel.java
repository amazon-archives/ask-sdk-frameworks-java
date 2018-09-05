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

import com.amazon.ask.interaction.model.Dialog;
import com.amazon.ask.interaction.model.InteractionModel;
import com.amazon.ask.interaction.model.Prompt;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fork of {@link InteractionModel}, removing invocation name.
 */
@JsonDeserialize(builder = SubModel.Builder.class)
public class SubModel {
    private final SubLanguageModel languageModel;
    private final Dialog dialog;
    private final List<Prompt> prompts;

    public SubModel(SubLanguageModel languageModel, Dialog dialog, List<Prompt> prompts) {
        if (languageModel == null) {
            throw new IllegalArgumentException("property 'languageModel' cannot be null");
        }
        this.languageModel = languageModel;
        this.dialog = dialog;
        this.prompts = prompts;
    }

    @JsonProperty("languageModel")
    public SubLanguageModel getLanguageModel() {
        return languageModel;
    }

    @JsonProperty("dialog")
    public Dialog getDialog() {
        return dialog;
    }

    @JsonProperty("prompts")
    public List<Prompt> getPrompts() {
        return prompts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubModel that = (SubModel) o;
        return Objects.equals(languageModel, that.languageModel) &&
            Objects.equals(dialog, that.dialog) &&
            Objects.equals(prompts, that.prompts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageModel, dialog, prompts);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SubLanguageModel languageModel;
        private Dialog dialog;
        private List<Prompt> prompts;

        @JsonProperty("languageModel")
        public Builder withLanguageModel(SubLanguageModel languageModel) {
            this.languageModel = languageModel;
            return this;
        }

        @JsonProperty("dialog")
        public Builder withDialog(Dialog dialog) {
            this.dialog = dialog;
            return this;
        }

        @JsonProperty("prompts")
        public Builder withPrompts(List<Prompt> prompts) {
            this.prompts = prompts;
            return this;
        }

        public Builder addPrompts(List<Prompt> prompts) {
            prompts.forEach(this::addPrompt);
            return this;
        }

        public Builder addPrompt(Prompt prompt) {
            if (this.prompts == null) {
                this.prompts = new ArrayList<>();
            }
            this.prompts.add(prompt);
            return this;
        }

        public SubModel build() {
            return new SubModel(languageModel, dialog, prompts);
        }
    }
}
