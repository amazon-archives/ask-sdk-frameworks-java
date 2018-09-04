/*
* Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
* except in compliance with the License. A copy of the License is located at
*
* http://aws.amazon.com/apache2.0/
*
* or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
* the specific language governing permissions and limitations under the License.
*/


package com.amazon.ask.interaction.model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * envelope containing the language model and dialog prompts
 */

@JsonDeserialize(builder = InteractionModel.Builder.class)
public final class InteractionModel{

  @JsonProperty("languageModel")
  private com.amazon.ask.interaction.model.LanguageModel languageModel = null;

  @JsonProperty("dialog")
  private com.amazon.ask.interaction.model.Dialog dialog = null;

  @JsonProperty("prompts")
  private List<com.amazon.ask.interaction.model.Prompt> prompts = new ArrayList<com.amazon.ask.interaction.model.Prompt>();

  public static Builder builder() {
    return new Builder();
  }

  private InteractionModel(Builder builder) {
    this.languageModel = builder.languageModel;
    this.dialog = builder.dialog;
    this.prompts = builder.prompts;
  }

  /**
    * conversational primitives for the skill
  * @return languageModel
  **/
  public com.amazon.ask.interaction.model.LanguageModel getLanguageModel() {
    return languageModel;
  }

  /**
    * rules for conducting a multi-turn dialog with the user
  * @return dialog
  **/
  public com.amazon.ask.interaction.model.Dialog getDialog() {
    return dialog;
  }

  /**
    * cues to the user on behalf of the skill for eliciting data or providing feedback
  * @return prompts
  **/
  public List<com.amazon.ask.interaction.model.Prompt> getPrompts() {
    return prompts;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InteractionModel interactionModel = (InteractionModel) o;
    return Objects.equals(this.languageModel, interactionModel.languageModel) &&
        Objects.equals(this.dialog, interactionModel.dialog) &&
        Objects.equals(this.prompts, interactionModel.prompts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(languageModel, dialog, prompts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InteractionModel {\n");
    
    sb.append("    languageModel: ").append(toIndentedString(languageModel)).append("\n");
    sb.append("    dialog: ").append(toIndentedString(dialog)).append("\n");
    sb.append("    prompts: ").append(toIndentedString(prompts)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public static class Builder {
    private com.amazon.ask.interaction.model.LanguageModel languageModel;
    private com.amazon.ask.interaction.model.Dialog dialog;
    private List<com.amazon.ask.interaction.model.Prompt> prompts;

    private Builder() { }

    @JsonProperty("languageModel")
    public Builder withLanguageModel(com.amazon.ask.interaction.model.LanguageModel languageModel) {
      this.languageModel = languageModel;
      return this;
    }
      

    @JsonProperty("dialog")
    public Builder withDialog(com.amazon.ask.interaction.model.Dialog dialog) {
      this.dialog = dialog;
      return this;
    }
      

    @JsonProperty("prompts")
    public Builder withPrompts(List<com.amazon.ask.interaction.model.Prompt> prompts) {
      this.prompts = prompts;
      return this;
    }
      
    public Builder addPromptsItem(com.amazon.ask.interaction.model.Prompt promptsItem) {
      if (this.prompts == null) {
        this.prompts = new ArrayList<com.amazon.ask.interaction.model.Prompt>();
      }
      this.prompts.add(promptsItem);
      return this;
    }

    public InteractionModel build() {
      return new InteractionModel(this);
    }
  }
}

