package com.amazon.ask.models.types.intent;

/**
 *
 */
public abstract class StandardIntent {
    StandardIntent() {} // seal

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
