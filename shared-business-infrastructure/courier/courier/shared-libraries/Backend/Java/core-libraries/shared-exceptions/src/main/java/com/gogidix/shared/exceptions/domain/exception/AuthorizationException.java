package com.gogidix.shared.exceptions.domain.exception;

/**
 * Authorization exception (403)
 * Security-related exception for authorization/permission failures
 */
public class AuthorizationException extends BaseException {

    private final String requiredPermission;

    /**
     * Constructor with message only
     */
    public AuthorizationException(String message) {
        super(message, "AUTHORIZATION_ERROR", 403, "SECURITY");
        this.requiredPermission = "Unknown";
    }

    /**
     * Constructor with message and error code OR required permission
     * If secondParam is "INSUFFICIENT_PERMISSIONS", it's used as error code
     * Otherwise, it's treated as required permission (for permissions like "ADMIN_ACCESS")
     */
    public AuthorizationException(String message, String secondParam) {
        this(message, secondParam, inferParamType(secondParam));
    }

    /**
     * Constructor with message, second param, and explicit type flag
     * @param isErrorCode true if secondParam is an error code, false if it's a required permission
     */
    private AuthorizationException(String message, String secondParam, boolean isErrorCode) {
        super(message, isErrorCode ? secondParam : "AUTHORIZATION_ERROR", 403, "SECURITY");
        this.requiredPermission = isErrorCode ? "Custom" : (secondParam != null ? secondParam : "Unknown");
        if (!isErrorCode) {
            addContext("requiredPermission", this.requiredPermission);
        }
    }

    /**
     * Constructor with message, error code, and required permission
     */
    public AuthorizationException(String message, String errorCode, String requiredPermission) {
        super(message, errorCode, 403, "SECURITY");
        this.requiredPermission = requiredPermission != null ? requiredPermission : "Unknown";
        addContext("requiredPermission", this.requiredPermission);
    }

    /**
     * Infers whether the second parameter is an error code or a permission name
     */
    private static boolean inferParamType(String param) {
        return "INSUFFICIENT_PERMISSIONS".equals(param) ||
               param != null && (param.endsWith("_ERROR") ||
                               param.equals("ACCESS_DENIED") ||
                               param.equals("FORBIDDEN") ||
                               param.equals("UNAUTHORIZED"));
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    @Override
    public boolean isCritical() {
        return false; // Authorization failures are not critical (expected operation)
    }

    @Override
    public boolean isRetryable() {
        return false; // Authorization failures are not retryable
    }
}
