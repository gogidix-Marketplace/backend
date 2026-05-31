package com.gogidix.corporatecms.application.exception;

/**
 * Exception thrown when attempting an invalid workflow transition.
 */
public class InvalidWorkflowTransitionException extends RuntimeException {

    private final String currentStatus;
    private final String targetStatus;

    public InvalidWorkflowTransitionException(String currentStatus, String targetStatus) {
        super(String.format("Invalid workflow transition from '%s' to '%s'", currentStatus, targetStatus));
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public InvalidWorkflowTransitionException(String message) {
        super(message);
        this.currentStatus = null;
        this.targetStatus = null;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}