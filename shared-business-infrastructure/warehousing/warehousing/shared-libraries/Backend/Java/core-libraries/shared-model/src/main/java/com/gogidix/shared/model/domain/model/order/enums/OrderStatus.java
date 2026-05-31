package com.gogidix.shared.model.domain.model.order.enums;

/**
 * Enumeration of possible order statuses throughout the order lifecycle.
 */
public enum OrderStatus {
    
    /**
     * Order has been created but not yet confirmed.
     */
    PENDING("Pending", "Order is awaiting confirmation"),
    
    /**
     * Order has been confirmed and is ready for processing.
     */
    CONFIRMED("Confirmed", "Order has been confirmed and accepted"),
    
    /**
     * Order is being processed (items being picked, packed).
     */
    PROCESSING("Processing", "Order is being processed for fulfillment"),
    
    /**
     * Order has been shipped and is in transit.
     */
    SHIPPED("Shipped", "Order has been shipped and is in transit"),
    
    /**
     * Order has been delivered to the customer.
     */
    DELIVERED("Delivered", "Order has been successfully delivered"),
    
    /**
     * Order has been completed (delivered and confirmed by customer).
     */
    COMPLETED("Completed", "Order is fully completed"),
    
    /**
     * Order has been cancelled before shipping.
     */
    CANCELLED("Cancelled", "Order has been cancelled"),
    
    /**
     * Order has been returned by the customer.
     */
    RETURNED("Returned", "Order has been returned"),
    
    /**
     * Order is on hold due to payment or other issues.
     */
    ON_HOLD("On Hold", "Order is temporarily on hold"),
    
    /**
     * Order has failed processing and needs attention.
     */
    FAILED("Failed", "Order processing has failed");
    
    private final String displayName;
    private final String description;
    
    OrderStatus(String displayName, String description) {
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
     * Checks if the order is in an active state.
     */
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING || this == SHIPPED;
    }
    
    /**
     * Checks if the order is in a final state.
     */
    public boolean isFinal() {
        return this == DELIVERED || this == COMPLETED || this == CANCELLED || this == RETURNED;
    }
    
    /**
     * Checks if the order can be modified.
     */
    public boolean allowsModification() {
        return this == PENDING || this == CONFIRMED;
    }
    
    /**
     * Checks if the order can be cancelled.
     */
    public boolean allowsCancellation() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING || this == ON_HOLD;
    }
    
    /**
     * Checks if the order requires payment.
     */
    public boolean requiresPayment() {
        return this == PENDING || this == ON_HOLD;
    }
    
    /**
     * Gets the next possible statuses from current status.
     */
    public OrderStatus[] getNextPossibleStatuses() {
        return switch (this) {
            case PENDING -> new OrderStatus[]{CONFIRMED, CANCELLED, ON_HOLD};
            case CONFIRMED -> new OrderStatus[]{PROCESSING, CANCELLED, ON_HOLD};
            case PROCESSING -> new OrderStatus[]{SHIPPED, CANCELLED, FAILED};
            case SHIPPED -> new OrderStatus[]{DELIVERED, RETURNED, FAILED};
            case DELIVERED -> new OrderStatus[]{COMPLETED, RETURNED};
            case ON_HOLD -> new OrderStatus[]{CONFIRMED, CANCELLED};
            case FAILED -> new OrderStatus[]{PROCESSING, CANCELLED};
            case COMPLETED, CANCELLED, RETURNED -> new OrderStatus[]{}; // Final states
        };
    }
    
    /**
     * Checks if transition to another status is valid.
     */
    public boolean canTransitionTo(OrderStatus newStatus) {
        OrderStatus[] possibleStatuses = getNextPossibleStatuses();
        for (OrderStatus status : possibleStatuses) {
            if (status == newStatus) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the completion percentage for this status.
     */
    public int getCompletionPercentage() {
        return switch (this) {
            case PENDING -> 0;
            case CONFIRMED -> 20;
            case PROCESSING -> 40;
            case SHIPPED -> 70;
            case DELIVERED -> 90;
            case COMPLETED -> 100;
            case ON_HOLD -> 10;
            case CANCELLED, RETURNED, FAILED -> 0;
        };
    }
    
    /**
     * Gets the priority level for processing (higher = more urgent).
     */
    public int getProcessingPriority() {
        return switch (this) {
            case FAILED -> 5; // Highest priority
            case ON_HOLD -> 4;
            case PENDING -> 3;
            case CONFIRMED -> 3;
            case PROCESSING -> 2;
            case SHIPPED -> 1;
            case DELIVERED, COMPLETED, CANCELLED, RETURNED -> 0; // No processing needed
        };
    }
}