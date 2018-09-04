package com.amazon.ask.mvc.mapper;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.module.SdkModuleContext;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.annotation.condition.WhenDialogState;
import com.amazon.ask.mvc.annotation.condition.WhenSessionAttribute;

import java.util.function.Predicate;

/**
 * Applies a priority ordinal to a handler.
 *
 * Used by the {@link MvcSdkModule#setupModule(SdkModuleContext)} to allow developers to affect the ordering of methods
 * since the JVM makes no guarantees of method ordering during reflection.
 *
 * We expect this to be only used in exceptional cases. It is recommended to constrain conflicting method handlers with
 * {@link Predicate<HandlerInput>} annotations, like {@link WhenSessionAttribute} and {@link WhenDialogState}. Mutually
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
