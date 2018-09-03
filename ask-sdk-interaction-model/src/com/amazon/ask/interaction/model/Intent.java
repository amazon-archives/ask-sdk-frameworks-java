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
 * Intent
 */

@JsonDeserialize(builder = Intent.Builder.class)
public final class Intent{

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("slots")
  private List<com.amazon.ask.interaction.model.Slot> slots = new ArrayList<com.amazon.ask.interaction.model.Slot>();

  @JsonProperty("samples")
  private List<String> samples = new ArrayList<String>();

  public static Builder builder() {
    return new Builder();
  }

  private Intent(Builder builder) {
    this.name = builder.name;
    this.slots = builder.slots;
    this.samples = builder.samples;
  }

  /**
    * name of the intent
  * @return name
  **/
  public String getName() {
    return name;
  }

  /**
    * list of slots within the intent
  * @return slots
  **/
  public List<com.amazon.ask.interaction.model.Slot> getSlots() {
    return slots;
  }

  /**
    * sample utterances for the intent
  * @return samples
  **/
  public List<String> getSamples() {
    return samples;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Intent intent = (Intent) o;
    return Objects.equals(this.name, intent.name) &&
        Objects.equals(this.slots, intent.slots) &&
        Objects.equals(this.samples, intent.samples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, slots, samples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Intent {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    samples: ").append(toIndentedString(samples)).append("\n");
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
    private List<com.amazon.ask.interaction.model.Slot> slots;
    private List<String> samples;

    private Builder() { }

    @JsonProperty("name")
    public Builder withName(String name) {
      this.name = name;
      return this;
    }
      

    @JsonProperty("slots")
    public Builder withSlots(List<com.amazon.ask.interaction.model.Slot> slots) {
      this.slots = slots;
      return this;
    }
      
    public Builder addSlotsItem(com.amazon.ask.interaction.model.Slot slotsItem) {
      if (this.slots == null) {
        this.slots = new ArrayList<com.amazon.ask.interaction.model.Slot>();
      }
      this.slots.add(slotsItem);
      return this;
    }

    @JsonProperty("samples")
    public Builder withSamples(List<String> samples) {
      this.samples = samples;
      return this;
    }
      
    public Builder addSamplesItem(String samplesItem) {
      if (this.samples == null) {
        this.samples = new ArrayList<String>();
      }
      this.samples.add(samplesItem);
      return this;
    }

    public Intent build() {
      return new Intent(this);
    }
  }
}

