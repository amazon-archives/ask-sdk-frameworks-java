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
 * envelope containing the interaction model
 */

@JsonDeserialize(builder = InteractionModelEnvelope.Builder.class)
public final class InteractionModelEnvelope {

  @JsonProperty("interactionModel")
  private com.amazon.ask.interaction.model.InteractionModel interactionModel = null;

  public static Builder builder() {
    return new Builder();
  }

  private InteractionModelEnvelope(Builder builder) {
    this.interactionModel = builder.interactionModel;
  }

  /**
    * Get interactionModel
  * @return interactionModel
  **/
  public com.amazon.ask.interaction.model.InteractionModel getInteractionModel() {
    return interactionModel;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InteractionModelEnvelope interactionModelEnvelope = (InteractionModelEnvelope) o;
    return Objects.equals(this.interactionModel, interactionModelEnvelope.interactionModel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interactionModel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillModel {\n");
    
    sb.append("    interactionModel: ").append(toIndentedString(interactionModel)).append("\n");
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
    private com.amazon.ask.interaction.model.InteractionModel interactionModel;

    private Builder() { }

    @JsonProperty("interactionModel")
    public Builder withInteractionModel(com.amazon.ask.interaction.model.InteractionModel interactionModel) {
      this.interactionModel = interactionModel;
      return this;
    }
      

    public InteractionModelEnvelope build() {
      return new InteractionModelEnvelope(this);
    }
  }
}

