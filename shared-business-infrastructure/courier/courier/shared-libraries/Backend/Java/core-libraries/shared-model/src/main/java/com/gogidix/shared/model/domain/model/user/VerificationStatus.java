package com.gogidix.shared.model.domain.model.user;

/**
 * Account Verification Status for compliance tracking
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B User Management
 * @version 1.0.0
 */
public enum VerificationStatus {
    NOT_STARTED("Verification not started"),
    PENDING("Verification in progress"),
    COMPLETED("Verification completed"),
    FAILED("Verification failed"),
    EXPIRED("Verification expired"),
    REJECTED("Verification rejected");
    
    private final String description;
    
    VerificationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isComplete() {
        return this == COMPLETED;
    }
    
    public boolean needsAction() {
        return this == PENDING || this == FAILED || this == EXPIRED;
    }
}