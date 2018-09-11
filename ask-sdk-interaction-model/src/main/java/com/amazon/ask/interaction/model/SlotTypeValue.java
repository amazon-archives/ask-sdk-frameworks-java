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
 * SlotTypeValue
 */

@JsonDeserialize(builder = SlotTypeValue.Builder.class)
public final class SlotTypeValue{

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private com.amazon.ask.interaction.model.SlotValue name = null;

  public static Builder builder() {
    return new Builder();
  }

  private SlotTypeValue(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
  }

  /**
    * identifier for a value of a custom slot type
  * @return id
  **/
  public String getId() {
    return id;
  }

  /**
    * describes a value of a custom slot type
  * @return name
  **/
  public com.amazon.ask.interaction.model.SlotValue getName() {
    return name;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlotTypeValue slotTypeValue = (SlotTypeValue) o;
    return Objects.equals(this.id, slotTypeValue.id) &&
        Objects.equals(this.name, slotTypeValue.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SlotTypeValue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
    private String id;
    private com.amazon.ask.interaction.model.SlotValue name;

    private Builder() { }

    @JsonProperty("id")
    public Builder withId(String id) {
      this.id = id;
      return this;
    }
      

    @JsonProperty("name")
    public Builder withName(com.amazon.ask.interaction.model.SlotValue name) {
      this.name = name;
      return this;
    }
      

    public SlotTypeValue build() {
      return new SlotTypeValue(this);
    }
  }
}

