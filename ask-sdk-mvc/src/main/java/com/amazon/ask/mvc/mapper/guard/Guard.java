/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper.guard;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.mvc.mapper.HasPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public abstract class Guard<T> implements HasPriority {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final T delegate;
    protected final Predicate<HandlerInput> predicate;
    protected final int priority;

    public Guard(T delegate, Predicate<HandlerInput> predicate, int priority) {
        this.delegate = assertNotNull(delegate, "delegate");
        this.predicate = assertNotNull(predicate, "predicate");
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public static abstract class Builder<Self extends Builder<Self, T, D>, T, D extends Guard<T>> {
        protected T delegate;
        protected Predicate<HandlerInput> predicate;
        protected int priority;

        protected Builder() {
        }

        public Self withDelegate(T delegate) {
            this.delegate = delegate;
            return getThis();
        }

        public Self withPredicate(Predicate<HandlerInput> predicate) {
            this.predicate = predicate;
            return getThis();
        }

        public Self withPriority(int priority) {
            this.priority = priority;
            return getThis();
        }

        @SuppressWarnings("unchecked")
        protected Self getThis() {
            return (Self) this;
        }

        public abstract D build();
    }
}
