/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper.invoke;

import java.lang.reflect.Method;

/**
 * Thrown when one of the specified parameters in an MVC controller cannot be resolved
 */
public class UnresolvedParameterException extends RuntimeException {
    private final int   index;
    private final Class type;

    public UnresolvedParameterException(int index, Class type, Method method) {
        super(String.format("Unable to resolve parameter at index '%d' of type '%s' in '%s:%s'", index, type.getName(), method.getDeclaringClass().getName(), method.getName()));
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public Class getType() {
        return type;
    }
}
