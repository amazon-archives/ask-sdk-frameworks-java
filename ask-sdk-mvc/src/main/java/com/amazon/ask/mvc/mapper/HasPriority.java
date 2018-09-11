/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper;

import com.amazon.ask.module.SdkModuleContext;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.annotation.condition.WhenDialogState;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;

/**
 * Applies a priority ordinal to a handler.
 *
 * Used by the {@link MvcSdkModule#setupModule(SdkModuleContext)} to allow developers to affect the ordering of methods
 * since the JVM makes no guarantees of method ordering during reflection.
 *
 * This should only be used in exceptional cases. It is preferable to constrain conflicting method handlers with
 * predicate annotations, like {@link WhenSessionAttribute} and {@link WhenDialogState}. Mutually
 * exclusive constraints are unaffected by ordering.
 *
 * @see MvcSdkModule
 * @see Priority
 */
public interface HasPriority {
    /**
     * Lower numbers are higher priority (descending ordering).
     *
     * @return priority of this handler.
     */
    int getPriority();
}
