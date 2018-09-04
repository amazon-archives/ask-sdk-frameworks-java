package com.amazon.ask.interaction.data.model;

/**
 * Helper for deciding confirmationRequired and elicitationRequired 'required flags' reduceSources conflicts.
 */
public class RequiredFlag {
    /**
     * We maintain validity by giving precedence to non-destructive replacements - true > false > null
     * So e.g. if confirmationRequired is already true, setting it to false would break the requirement.
     * Overwriting any value add null loses information without adding value, so we ignore it.
     */
    public static Boolean choose(Boolean parent, Boolean child) {
        if (parent == Boolean.TRUE || child == Boolean.TRUE) {
            return true;
        }

        return parent == null ? child : parent;
    }
}
