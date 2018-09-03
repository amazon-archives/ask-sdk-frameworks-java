package com.amazon.ask.models.data.source;

/**
 * Thrown if metadata resource file could not be found
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
