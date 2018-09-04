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
 * Slot
 */

@JsonDeserialize(builder = Slot.Builder.class)
public final class Slot{

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("samples")
  private List<String> samples = new ArrayList<String>();

  public static Builder builder() {
    return new Builder();
  }

  private Slot(Builder builder) {
    this.name = builder.name;
    this.type = builder.type;
    this.samples = builder.samples;
  }

  /**
    * name of the slot
  * @return name
  **/
  public String getName() {
    return name;
  }

  /**
    * type of the slot
  * @return type
  **/
  public String getType() {
    return type;
  }

  /**
    * Get samples
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
    Slot slot = (Slot) o;
    return Objects.equals(this.name, slot.name) &&
        Objects.equals(this.type, slot.type) &&
        Objects.equals(this.samples, slot.samples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, samples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Slot {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
    private String type;
    private List<String> samples;

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

    public Slot build() {
      return new Slot(this);
    }
  }
}

