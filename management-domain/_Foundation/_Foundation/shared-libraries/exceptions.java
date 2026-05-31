package com.gogidix.management.shared.exception;

/**
 * Base exception for all domain exceptions.
 */
public abstract class DomainException extends RuntimeException {

    private final String errorCode;

    public DomainException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }

    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends DomainException {

    public NotFoundException(String resource, String id) {
        super("NOT_FOUND", String.format("%s not found: %s", resource, id));
    }

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }

    public NotFoundException(String resource, String id, Throwable cause) {
        super("NOT_FOUND", String.format("%s not found: %s", resource, id), cause);
    }
}

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }

    public ValidationException(String field, String reason) {
        super("VALIDATION_ERROR", String.format("Validation failed for field '%s': %s", field, reason));
    }

    public ValidationException(String message, Throwable cause) {
        super("VALIDATION_ERROR", message, cause);
    }
}

/**
 * Exception thrown when there's a conflict with existing data.
 */
public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super("CONFLICT", message);
    }

    public ConflictException(String resource, String id) {
        super("CONFLICT", String.format("%s already exists: %s", resource, id));
    }

    public ConflictException(String message, Throwable cause) {
        super("CONFLICT", message, cause);
    }
}

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessException extends DomainException {

    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }

    public BusinessException(String message, Throwable cause) {
        super("BUSINESS_ERROR", message, cause);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }
}

/**
 * Exception thrown when tenant validation fails.
 */
public class TenantNotActiveException extends DomainException {

    public TenantNotActiveException(String tenantId) {
        super("TENANT_NOT_ACTIVE", String.format("Tenant '%s' is not active", tenantId));
    }
}

/**
 * Exception thrown when access is denied.
 */
public class AccessDeniedException extends DomainException {

    public AccessDeniedException(String message) {
        super("ACCESS_DENIED", message);
    }

    public AccessDeniedException(String resource, String action) {
        super("ACCESS_DENIED", String.format("Access denied to %s for action: %s", resource, action));
    }
}
