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
 * conversational primitives for the skill
 */

@JsonDeserialize(builder = LanguageModel.Builder.class)
public final class LanguageModel{

  @JsonProperty("invocationName")
  private String invocationName = null;

  @JsonProperty("intents")
  private List<com.amazon.ask.interaction.model.Intent> intents = new ArrayList<com.amazon.ask.interaction.model.Intent>();

  @JsonProperty("types")
  private List<com.amazon.ask.interaction.model.SlotType> types = new ArrayList<com.amazon.ask.interaction.model.SlotType>();

  public static Builder builder() {
    return new Builder();
  }

  private LanguageModel(Builder builder) {
    this.invocationName = builder.invocationName;
    this.intents = builder.intents;
    this.types = builder.types;
  }

  /**
    * invocation name of the skill
  * @return invocationName
  **/
  public String getInvocationName() {
    return invocationName;
  }

  /**
    * intents and their slots
  * @return intents
  **/
  public List<com.amazon.ask.interaction.model.Intent> getIntents() {
    return intents;
  }

  /**
    * custom slot types
  * @return types
  **/
  public List<com.amazon.ask.interaction.model.SlotType> getTypes() {
    return types;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LanguageModel languageModel = (LanguageModel) o;
    return Objects.equals(this.invocationName, languageModel.invocationName) &&
        Objects.equals(this.intents, languageModel.intents) &&
        Objects.equals(this.types, languageModel.types);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invocationName, intents, types);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LanguageModel {\n");
    
    sb.append("    invocationName: ").append(toIndentedString(invocationName)).append("\n");
    sb.append("    intents: ").append(toIndentedString(intents)).append("\n");
    sb.append("    types: ").append(toIndentedString(types)).append("\n");
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
    private String invocationName;
    private List<com.amazon.ask.interaction.model.Intent> intents;
    private List<com.amazon.ask.interaction.model.SlotType> types;

    private Builder() { }

    @JsonProperty("invocationName")
    public Builder withInvocationName(String invocationName) {
      this.invocationName = invocationName;
      return this;
    }
      

    @JsonProperty("intents")
    public Builder withIntents(List<com.amazon.ask.interaction.model.Intent> intents) {
      this.intents = intents;
      return this;
    }
      
    public Builder addIntentsItem(com.amazon.ask.interaction.model.Intent intentsItem) {
      if (this.intents == null) {
        this.intents = new ArrayList<com.amazon.ask.interaction.model.Intent>();
      }
      this.intents.add(intentsItem);
      return this;
    }

    @JsonProperty("types")
    public Builder withTypes(List<com.amazon.ask.interaction.model.SlotType> types) {
      this.types = types;
      return this;
    }
      
    public Builder addTypesItem(com.amazon.ask.interaction.model.SlotType typesItem) {
      if (this.types == null) {
        this.types = new ArrayList<com.amazon.ask.interaction.model.SlotType>();
      }
      this.types.add(typesItem);
      return this;
    }

    public LanguageModel build() {
      return new LanguageModel(this);
    }
  }
}

