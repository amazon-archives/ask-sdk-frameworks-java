/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.controller;

import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;

public class ReturnTypeController {
    @IntentMapping(name = "voidReturnType")
    public void voidReturnType() {}

    @IntentMapping(name = "badResultType")
    public Object badResultType() {
        return new Object();
    }

    @IntentMapping(name = "nullResultValue")
    public Object nullResultValue() {
        return null;
    }

    @IntentMapping(name = "speechletResponseResultValue")
    public Object speechletResponseResultValue() {
        return Utils.EMPTY_RESPONSE;
    }

}
