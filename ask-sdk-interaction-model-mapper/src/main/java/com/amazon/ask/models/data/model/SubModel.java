package com.amazon.ask.models.data.model;

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
