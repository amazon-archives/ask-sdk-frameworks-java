/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.interaction.mapper.IntentParseException;

public interface IntentReader<T> {
    /**
     * Parse an IntentRequest to a concrete instance
     *
     * @param intentRequest intent request
     * @return instance representation of this request
     * @throws IntentParseException if an instance could not be read from the request
     */
    T read(IntentRequest intentRequest) throws IntentParseException;
}
