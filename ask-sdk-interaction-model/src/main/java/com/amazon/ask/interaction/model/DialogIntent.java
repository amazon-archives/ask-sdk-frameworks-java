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
 * dialog data for eliciting or confirming information from a user
 */

@JsonDeserialize(builder = DialogIntent.Builder.class)
public final class DialogIntent{

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("confirmationRequired")
  private Boolean confirmationRequired = null;

  @JsonProperty("prompts")
  private com.amazon.ask.interaction.model.DialogIntentPrompt prompts = null;

  @JsonProperty("slots")
  private List<com.amazon.ask.interaction.model.DialogSlot> slots = new ArrayList<com.amazon.ask.interaction.model.DialogSlot>();

  public static Builder builder() {
    return new Builder();
  }

  private DialogIntent(Builder builder) {
    this.name = builder.name;
    this.confirmationRequired = builder.confirmationRequired;
    this.prompts = builder.prompts;
    this.slots = builder.slots;
  }

  /**
    * name of the intent that has dialog rules
  * @return name
  **/
  public String getName() {
    return name;
  }

  /**
    * describes whether confirmation of the intent is required
  * @return confirmationRequired
  **/
  public Boolean getConfirmationRequired() {
    return confirmationRequired;
  }

  /**
    * collection of prompts for this intent
  * @return prompts
  **/
  public com.amazon.ask.interaction.model.DialogIntentPrompt getPrompts() {
    return prompts;
  }

  /**
    * list of slots in this intent that have dialog rules
  * @return slots
  **/
  public List<com.amazon.ask.interaction.model.DialogSlot> getSlots() {
    return slots;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogIntent dialogIntent = (DialogIntent) o;
    return Objects.equals(this.name, dialogIntent.name) &&
        Objects.equals(this.confirmationRequired, dialogIntent.confirmationRequired) &&
        Objects.equals(this.prompts, dialogIntent.prompts) &&
        Objects.equals(this.slots, dialogIntent.slots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, confirmationRequired, prompts, slots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogIntent {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    confirmationRequired: ").append(toIndentedString(confirmationRequired)).append("\n");
    sb.append("    prompts: ").append(toIndentedString(prompts)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
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
    private String name;
    private Boolean confirmationRequired;
    private com.amazon.ask.interaction.model.DialogIntentPrompt prompts;
    private List<com.amazon.ask.interaction.model.DialogSlot> slots;

    private Builder() { }

    @JsonProperty("name")
    public Builder withName(String name) {
      this.name = name;
      return this;
    }
      

    @JsonProperty("confirmationRequired")
    public Builder withConfirmationRequired(Boolean confirmationRequired) {
      this.confirmationRequired = confirmationRequired;
      return this;
    }
      

    @JsonProperty("prompts")
    public Builder withPrompts(com.amazon.ask.interaction.model.DialogIntentPrompt prompts) {
      this.prompts = prompts;
      return this;
    }
      

    @JsonProperty("slots")
    public Builder withSlots(List<com.amazon.ask.interaction.model.DialogSlot> slots) {
      this.slots = slots;
      return this;
    }
      
    public Builder addSlotsItem(com.amazon.ask.interaction.model.DialogSlot slotsItem) {
      if (this.slots == null) {
        this.slots = new ArrayList<com.amazon.ask.interaction.model.DialogSlot>();
      }
      this.slots.add(slotsItem);
      return this;
    }

    public DialogIntent build() {
      return new DialogIntent(this);
    }
  }
}

