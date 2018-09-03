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
 * prompt identifiers for confirming or eliciting a slot value
 */

@JsonDeserialize(builder = DialogSlotPrompt.Builder.class)
public final class DialogSlotPrompt{

  @JsonProperty("elicitation")
  private String elicitation = null;

  @JsonProperty("confirmation")
  private String confirmation = null;

  public static Builder builder() {
    return new Builder();
  }

  private DialogSlotPrompt(Builder builder) {
    this.elicitation = builder.elicitation;
    this.confirmation = builder.confirmation;
  }

  /**
    * enum value in the dialog_slots map to reference the elicitation prompt id
  * @return elicitation
  **/
  public String getElicitation() {
    return elicitation;
  }

  /**
    * enum value in the dialog_slots map to reference the confirmation prompt id
  * @return confirmation
  **/
  public String getConfirmation() {
    return confirmation;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogSlotPrompt dialogSlotPrompt = (DialogSlotPrompt) o;
    return Objects.equals(this.elicitation, dialogSlotPrompt.elicitation) &&
        Objects.equals(this.confirmation, dialogSlotPrompt.confirmation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elicitation, confirmation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogSlotPrompt {\n");
    
    sb.append("    elicitation: ").append(toIndentedString(elicitation)).append("\n");
    sb.append("    confirmation: ").append(toIndentedString(confirmation)).append("\n");
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
    private String elicitation;
    private String confirmation;

    private Builder() { }

    @JsonProperty("elicitation")
    public Builder withElicitation(String elicitation) {
      this.elicitation = elicitation;
      return this;
    }
      

    @JsonProperty("confirmation")
    public Builder withConfirmation(String confirmation) {
      this.confirmation = confirmation;
      return this;
    }
      

    public DialogSlotPrompt build() {
      return new DialogSlotPrompt(this);
    }
  }
}

