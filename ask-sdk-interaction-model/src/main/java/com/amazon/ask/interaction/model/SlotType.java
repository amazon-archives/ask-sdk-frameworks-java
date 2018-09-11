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
 * SlotType
 */

@JsonDeserialize(builder = SlotType.Builder.class)
public final class SlotType{

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("values")
  private List<com.amazon.ask.interaction.model.SlotTypeValue> values = new ArrayList<com.amazon.ask.interaction.model.SlotTypeValue>();

  public static Builder builder() {
    return new Builder();
  }

  private SlotType(Builder builder) {
    this.name = builder.name;
    this.values = builder.values;
  }

  /**
    * name of the custom slot type
  * @return name
  **/
  public String getName() {
    return name;
  }

  /**
    * list of representative values for the slot
  * @return values
  **/
  public List<com.amazon.ask.interaction.model.SlotTypeValue> getValues() {
    return values;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlotType slotType = (SlotType) o;
    return Objects.equals(this.name, slotType.name) &&
        Objects.equals(this.values, slotType.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, values);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SlotType {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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
    private List<com.amazon.ask.interaction.model.SlotTypeValue> values;

    private Builder() { }

    @JsonProperty("name")
    public Builder withName(String name) {
      this.name = name;
      return this;
    }
      

    @JsonProperty("values")
    public Builder withValues(List<com.amazon.ask.interaction.model.SlotTypeValue> values) {
      this.values = values;
      return this;
    }
      
    public Builder addValuesItem(com.amazon.ask.interaction.model.SlotTypeValue valuesItem) {
      if (this.values == null) {
        this.values = new ArrayList<com.amazon.ask.interaction.model.SlotTypeValue>();
      }
      this.values.add(valuesItem);
      return this;
    }

    public SlotType build() {
      return new SlotType(this);
    }
  }
}

