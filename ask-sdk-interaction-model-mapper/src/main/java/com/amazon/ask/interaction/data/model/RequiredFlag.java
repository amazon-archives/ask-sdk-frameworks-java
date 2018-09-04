package com.amazon.ask.interaction.data.model;

/**
 * Helper for deciding confirmationRequired and elicitationRequired 'required flags' reduceSources conflicts.
 */
public class RequiredFlag {
    /**
     * We maintain validity by giving precedence to non-destructive replacements - true {@literal >} false {@literal >} null
     * So e.g. if confirmationRequired is already true, setting it to false would break the requirement.
     * Overwriting any value add null loses information without adding value, so we ignore it.
     *
     * @param parent optional boolean value of parent's value
     * @param child optional boolean value of child's value
     * @return true if parent or child is true
     *         false if parent or child is false
     *         otherwise null
     */
    public static Boolean choose(Boolean parent, Boolean child) {
        if (parent == Boolean.TRUE || child == Boolean.TRUE) {
            return true;
        }

        return parent == null ? child : parent;
    }
}
