package de.malte.event.enums;

public enum ErrorHandling {
    /**
     * Throws the given Exception wrapped in an RuntimeException.
     */
    THROW,
    /**
     * Just prints the Exception Stacktrace into console.
     */
    LOG
}
