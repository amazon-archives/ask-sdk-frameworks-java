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
 * prompt speech
 */

@JsonDeserialize(builder = PromptVariation.Builder.class)
public final class PromptVariation{

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("value")
  private String value = null;

  public static Builder builder() {
    return new Builder();
  }

  private PromptVariation(Builder builder) {
    this.type = builder.type;
    this.value = builder.value;
  }

  /**
    * one of PlainText or SSML
  * @return type
  **/
  public String getType() {
    return type;
  }

  /**
    * text that Alexa says as a prompt.
  * @return value
  **/
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PromptVariation promptVariation = (PromptVariation) o;
    return Objects.equals(this.type, promptVariation.type) &&
        Objects.equals(this.value, promptVariation.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PromptVariation {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
    private String type;
    private String value;

    private Builder() { }

    @JsonProperty("type")
    public Builder withType(String type) {
      this.type = type;
      return this;
    }
      

    @JsonProperty("value")
    public Builder withValue(String value) {
      this.value = value;
      return this;
    }
      

    public PromptVariation build() {
      return new PromptVariation(this);
    }
  }
}

