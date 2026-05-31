package com.gogidix.shared.exceptions;

import java.util.List;

/**
 * Facade for ValidationException - provides simpler import path.
 * <p>
 * This class provides backward compatibility for services expecting the simpler
 * package structure. All functionality is delegated to the canonical
 * {@link com.gogidix.shared.exceptions.domain.exception.ValidationException} class
 * in the domain.exception package.
 * </p>
 *
 * @deprecated Use {@link com.gogidix.shared.exceptions.domain.exception.ValidationException} instead.
 *             This facade exists for backward compatibility with existing service imports.
 */
@Deprecated
public class ValidationException extends com.gogidix.shared.exceptions.domain.exception.ValidationException {

    /**
     * Creates a new ValidationException with a message.
     *
     * @param message the error message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Creates a new ValidationException with a message and list of validation errors.
     *
     * @param message the error message
     * @param validationErrors the list of validation errors
     */
    public ValidationException(String message, List<String> validationErrors) {
        super(message, validationErrors);
    }

    /**
     * Static factory method for consistency with domain pattern.
     *
     * @param message the error message
     * @return a new ValidationException instance
     */
    public static ValidationException of(String message) {
        return new ValidationException(message);
    }

    /**
     * Static factory method with validation errors.
     *
     * @param message the error message
     * @param validationErrors the list of validation errors
     * @return a new ValidationException instance
     */
    public static ValidationException of(String message, List<String> validationErrors) {
        return new ValidationException(message, validationErrors);
    }
}
