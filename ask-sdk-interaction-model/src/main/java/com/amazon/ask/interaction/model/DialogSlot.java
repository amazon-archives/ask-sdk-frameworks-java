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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * dialog data for confirming or eliciting a slot value
 */

@JsonDeserialize(builder = DialogSlot.Builder.class)
public final class DialogSlot{

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("confirmationRequired")
  private Boolean confirmationRequired = null;

  @JsonProperty("elicitationRequired")
  private Boolean elicitationRequired = null;

  @JsonProperty("prompts")
  private com.amazon.ask.interaction.model.DialogSlotPrompt prompts = null;

  public static Builder builder() {
    return new Builder();
  }

  private DialogSlot(Builder builder) {
    this.name = builder.name;
    this.type = builder.type;
    this.confirmationRequired = builder.confirmationRequired;
    this.elicitationRequired = builder.elicitationRequired;
    this.prompts = builder.prompts;
  }

  /**
    * name of the slot in the dialog intent
  * @return name
  **/
  public String getName() {
    return name;
  }

  /**
    * type of the slot in the dialog intent
  * @return type
  **/
  public String getType() {
    return type;
  }

  /**
    * describes whether confirmation of the slot is required
  * @return confirmationRequired
  **/
  public Boolean getConfirmationRequired() {
    return confirmationRequired;
  }

  /**
    * describes whether elicitation of the slot is required
  * @return elicitationRequired
  **/
  public Boolean getElicitationRequired() {
    return elicitationRequired;
  }

  /**
    * references to this slot's prompts
  * @return prompts
  **/
  public com.amazon.ask.interaction.model.DialogSlotPrompt getPrompts() {
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
    DialogSlot dialogSlot = (DialogSlot) o;
    return Objects.equals(this.name, dialogSlot.name) &&
        Objects.equals(this.type, dialogSlot.type) &&
        Objects.equals(this.confirmationRequired, dialogSlot.confirmationRequired) &&
        Objects.equals(this.elicitationRequired, dialogSlot.elicitationRequired) &&
        Objects.equals(this.prompts, dialogSlot.prompts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, confirmationRequired, elicitationRequired, prompts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogSlot {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    confirmationRequired: ").append(toIndentedString(confirmationRequired)).append("\n");
    sb.append("    elicitationRequired: ").append(toIndentedString(elicitationRequired)).append("\n");
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
    private String name;
    private String type;
    private Boolean confirmationRequired;
    private Boolean elicitationRequired;
    private com.amazon.ask.interaction.model.DialogSlotPrompt prompts;

    private Builder() { }

    @JsonProperty("name")
    public Builder withName(String name) {
      this.name = name;
      return this;
    }
      

    @JsonProperty("type")
    public Builder withType(String type) {
      this.type = type;
      return this;
    }
      

    @JsonProperty("confirmationRequired")
    public Builder withConfirmationRequired(Boolean confirmationRequired) {
      this.confirmationRequired = confirmationRequired;
      return this;
    }
      

    @JsonProperty("elicitationRequired")
    public Builder withElicitationRequired(Boolean elicitationRequired) {
      this.elicitationRequired = elicitationRequired;
      return this;
    }
      

    @JsonProperty("prompts")
    public Builder withPrompts(com.amazon.ask.interaction.model.DialogSlotPrompt prompts) {
      this.prompts = prompts;
      return this;
    }
      

    public DialogSlot build() {
      return new DialogSlot(this);
    }
  }
}

