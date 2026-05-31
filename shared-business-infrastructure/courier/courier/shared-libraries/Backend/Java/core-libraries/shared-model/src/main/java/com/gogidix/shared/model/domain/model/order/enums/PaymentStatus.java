package com.gogidix.shared.model.domain.model.order.enums;

/**
 * Enumeration of payment statuses for orders.
 */
public enum PaymentStatus {
    
    /**
     * Payment is pending and not yet processed.
     */
    PENDING("Pending", "Payment is awaiting processing"),
    
    /**
     * Payment is being processed.
     */
    PROCESSING("Processing", "Payment is being processed"),
    
    /**
     * Payment has been successfully completed.
     */
    COMPLETED("Completed", "Payment has been successfully processed"),
    
    /**
     * Payment attempt failed.
     */
    FAILED("Failed", "Payment processing failed"),
    
    /**
     * Payment has been refunded.
     */
    REFUNDED("Refunded", "Payment has been refunded"),
    
    /**
     * Partial refund has been issued.
     */
    PARTIALLY_REFUNDED("Partially Refunded", "Payment has been partially refunded"),
    
    /**
     * Payment is disputed by customer.
     */
    DISPUTED("Disputed", "Payment is under dispute"),
    
    /**
     * Payment was cancelled before processing.
     */
    CANCELLED("Cancelled", "Payment was cancelled"),
    
    /**
     * Payment is on hold for security review.
     */
    ON_HOLD("On Hold", "Payment is on hold for review");
    
    private final String displayName;
    private final String description;
    
    PaymentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if payment is successful.
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }
    
    /**
     * Checks if payment is in a final state.
     */
    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == REFUNDED || this == CANCELLED;
    }
    
    /**
     * Checks if payment can be refunded.
     */
    public boolean canBeRefunded() {
        return this == COMPLETED || this == PARTIALLY_REFUNDED;
    }
    
    /**
     * Checks if payment requires attention.
     */
    public boolean requiresAttention() {
        return this == FAILED || this == DISPUTED || this == ON_HOLD;
    }
    
    /**
     * Gets the next possible payment statuses.
     */
    public PaymentStatus[] getNextPossibleStatuses() {
        return switch (this) {
            case PENDING -> new PaymentStatus[]{PROCESSING, CANCELLED, ON_HOLD};
            case PROCESSING -> new PaymentStatus[]{COMPLETED, FAILED, ON_HOLD};
            case COMPLETED -> new PaymentStatus[]{REFUNDED, PARTIALLY_REFUNDED, DISPUTED};
            case FAILED -> new PaymentStatus[]{PROCESSING, CANCELLED};
            case ON_HOLD -> new PaymentStatus[]{PROCESSING, CANCELLED};
            case PARTIALLY_REFUNDED -> new PaymentStatus[]{REFUNDED, DISPUTED};
            case DISPUTED -> new PaymentStatus[]{COMPLETED, REFUNDED};
            case REFUNDED, CANCELLED -> new PaymentStatus[]{}; // Final states
        };
    }
    
    /**
     * Checks if transition to another payment status is valid.
     */
    public boolean canTransitionTo(PaymentStatus newStatus) {
        PaymentStatus[] possibleStatuses = getNextPossibleStatuses();
        for (PaymentStatus status : possibleStatuses) {
            if (status == newStatus) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the risk level for this payment status.
     */
    public RiskLevel getRiskLevel() {
        return switch (this) {
            case DISPUTED -> RiskLevel.HIGH;
            case FAILED, ON_HOLD -> RiskLevel.MEDIUM;
            case PROCESSING, PENDING -> RiskLevel.LOW;
            case COMPLETED, REFUNDED, PARTIALLY_REFUNDED, CANCELLED -> RiskLevel.NONE;
        };
    }
    
    public enum RiskLevel {
        NONE, LOW, MEDIUM, HIGH
    }
}