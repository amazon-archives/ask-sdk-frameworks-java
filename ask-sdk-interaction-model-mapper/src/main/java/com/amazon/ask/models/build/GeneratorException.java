package com.amazon.ask.models.build;

/**
 * Thrown if a model could not be generated.
 */
public class GeneratorException extends Exception {
    public GeneratorException(Throwable cause) {
        super(cause);
    }
}
