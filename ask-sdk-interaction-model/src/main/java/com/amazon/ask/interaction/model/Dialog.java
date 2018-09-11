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
 * Dialog
 */

@JsonDeserialize(builder = Dialog.Builder.class)
public final class Dialog{

  @JsonProperty("intents")
  private List<com.amazon.ask.interaction.model.DialogIntent> intents = new ArrayList<com.amazon.ask.interaction.model.DialogIntent>();

  public static Builder builder() {
    return new Builder();
  }

  private Dialog(Builder builder) {
    this.intents = builder.intents;
  }

  /**
    * list of intents that have dialog rules associated with them
  * @return intents
  **/
  public List<com.amazon.ask.interaction.model.DialogIntent> getIntents() {
    return intents;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Dialog dialog = (Dialog) o;
    return Objects.equals(this.intents, dialog.intents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(intents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dialog {\n");
    
    sb.append("    intents: ").append(toIndentedString(intents)).append("\n");
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
    private List<com.amazon.ask.interaction.model.DialogIntent> intents;

    private Builder() { }

    @JsonProperty("intents")
    public Builder withIntents(List<com.amazon.ask.interaction.model.DialogIntent> intents) {
      this.intents = intents;
      return this;
    }
      
    public Builder addIntentsItem(com.amazon.ask.interaction.model.DialogIntent intentsItem) {
      if (this.intents == null) {
        this.intents = new ArrayList<com.amazon.ask.interaction.model.DialogIntent>();
      }
      this.intents.add(intentsItem);
      return this;
    }

    public Dialog build() {
      return new Dialog(this);
    }
  }
}

