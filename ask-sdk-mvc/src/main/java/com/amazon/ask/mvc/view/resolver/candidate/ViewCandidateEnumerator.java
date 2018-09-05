/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view.resolver.candidate;

import com.amazon.ask.model.RequestEnvelope;

import java.util.stream.Stream;

/**
 * Enumerate through view candidates in order of preference for this particular request.
 *
 * @see LocaleViewCandidateEnumerator
 */
public interface ViewCandidateEnumerator {
    Stream<String> enumerate(String viewName, RequestEnvelope requestEnvelope);
}
