package com.gogidix.shared.exceptions;

import com.gogidix.shared.exceptions.domain.exception.ResourceNotFoundException;

/**
 * Facade for ResourceNotFoundException - provides simpler import path.
 * <p>
 * This class provides backward compatibility for services expecting the simpler
 * package structure. All functionality is delegated to the canonical
 * {@link ResourceNotFoundException} class in the domain.exception package.
 * </p>
 *
 * @deprecated Use {@link com.gogidix.shared.exceptions.domain.exception.ResourceNotFoundException} instead.
 *             This facade exists for backward compatibility with existing service imports.
 */
@Deprecated
public class NotFoundException extends ResourceNotFoundException {

    /**
     * Creates a new NotFoundException with resource type and ID.
     *
     * @param resourceType the type of resource that was not found
     * @param resourceId the ID of the resource that was not found
     */
    public NotFoundException(String resourceType, String resourceId) {
        super(resourceType, resourceId);
    }

    /**
     * Creates a new NotFoundException with a custom message.
     *
     * @param message the error message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Static factory method for consistency with domain pattern.
     *
     * @param resourceType the type of resource that was not found
     * @param resourceId the ID of the resource that was not found
     * @return a new NotFoundException instance
     */
    public static NotFoundException of(String resourceType, String resourceId) {
        return new NotFoundException(resourceType, resourceId);
    }
}
