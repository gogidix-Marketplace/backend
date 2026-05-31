package com.gogidix.shared.model.domain.model.order.enums;

/**
 * Enumeration of shipping methods available for orders.
 */
public enum ShippingMethod {
    
    /**
     * Standard ground shipping (5-7 business days).
     */
    STANDARD("Standard Shipping", "5-7 business days", 5.99, 7),
    
    /**
     * Express shipping (2-3 business days).
     */
    EXPRESS("Express Shipping", "2-3 business days", 12.99, 3),
    
    /**
     * Overnight shipping (next business day).
     */
    OVERNIGHT("Overnight Shipping", "Next business day", 24.99, 1),
    
    /**
     * Same day delivery (within hours).
     */
    SAME_DAY("Same Day Delivery", "Same day delivery", 19.99, 0),
    
    /**
     * Free standard shipping (7-10 business days).
     */
    FREE_STANDARD("Free Standard Shipping", "7-10 business days", 0.00, 10),
    
    /**
     * Two-day priority shipping.
     */
    TWO_DAY("Two Day Shipping", "2 business days", 15.99, 2),
    
    /**
     * International standard shipping.
     */
    INTERNATIONAL_STANDARD("International Standard", "10-21 business days", 29.99, 21),
    
    /**
     * International express shipping.
     */
    INTERNATIONAL_EXPRESS("International Express", "3-7 business days", 59.99, 7),
    
    /**
     * Store pickup option.
     */
    STORE_PICKUP("Store Pickup", "Ready in 2-4 hours", 0.00, 0),
    
    /**
     * Curbside pickup option.
     */
    CURBSIDE_PICKUP("Curbside Pickup", "Ready in 2-4 hours", 0.00, 0),
    
    /**
     * White glove delivery service.
     */
    WHITE_GLOVE("White Glove Delivery", "Scheduled delivery with setup", 99.99, 5),
    
    /**
     * Freight shipping for large items.
     */
    FREIGHT("Freight Shipping", "5-10 business days", 149.99, 10);
    
    private final String displayName;
    private final String description;
    private final double baseCost;
    private final int deliveryDays;
    
    ShippingMethod(String displayName, String description, double baseCost, int deliveryDays) {
        this.displayName = displayName;
        this.description = description;
        this.baseCost = baseCost;
        this.deliveryDays = deliveryDays;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getBaseCost() {
        return baseCost;
    }
    
    public int getDeliveryDays() {
        return deliveryDays;
    }
    
    /**
     * Checks if this is a free shipping method.
     */
    public boolean isFree() {
        return baseCost == 0.00;
    }
    
    /**
     * Checks if this is an express shipping method.
     */
    public boolean isExpress() {
        return deliveryDays <= 2 && this != STORE_PICKUP && this != CURBSIDE_PICKUP;
    }
    
    /**
     * Checks if this is an international shipping method.
     */
    public boolean isInternational() {
        return this == INTERNATIONAL_STANDARD || this == INTERNATIONAL_EXPRESS;
    }
    
    /**
     * Checks if this requires physical pickup.
     */
    public boolean requiresPickup() {
        return this == STORE_PICKUP || this == CURBSIDE_PICKUP;
    }
    
    /**
     * Checks if this is a premium service.
     */
    public boolean isPremiumService() {
        return this == WHITE_GLOVE || this == FREIGHT;
    }
    
    /**
     * Gets the speed category.
     */
    public SpeedCategory getSpeedCategory() {
        if (deliveryDays == 0) return SpeedCategory.SAME_DAY;
        if (deliveryDays <= 1) return SpeedCategory.OVERNIGHT;
        if (deliveryDays <= 3) return SpeedCategory.EXPRESS;
        if (deliveryDays <= 7) return SpeedCategory.STANDARD;
        return SpeedCategory.ECONOMY;
    }
    
    /**
     * Gets the service level.
     */
    public ServiceLevel getServiceLevel() {
        if (isPremiumService()) return ServiceLevel.PREMIUM;
        if (isExpress()) return ServiceLevel.EXPRESS;
        if (isFree()) return ServiceLevel.ECONOMY;
        return ServiceLevel.STANDARD;
    }
    
    /**
     * Calculates shipping cost with weight multiplier.
     */
    public double calculateCost(double weight, boolean isPriority) {
        double cost = baseCost;
        
        // Apply weight multiplier for non-pickup methods
        if (!requiresPickup()) {
            if (weight > 1.0) {
                cost += (weight - 1.0) * 2.50; // $2.50 per additional pound
            }
        }
        
        // Apply priority multiplier
        if (isPriority && !isExpress()) {
            cost *= 1.25; // 25% upcharge for priority
        }
        
        return cost;
    }
    
    /**
     * Checks if this method is available for the given criteria.
     */
    public boolean isAvailableFor(boolean isInternational, double weight, boolean isPerishable) {
        // International check
        if (isInternational && !this.isInternational() && !requiresPickup()) {
            return false;
        }
        
        // Weight restrictions
        if (weight > 50.0 && this != FREIGHT) {
            return false;
        }
        
        // Perishable items need faster shipping
        if (isPerishable && deliveryDays > 3) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets recommended shipping methods based on criteria.
     */
    public static ShippingMethod[] getRecommendedMethods(boolean isInternational, double weight, 
                                                       boolean isPerishable, double orderValue) {
        return java.util.Arrays.stream(ShippingMethod.values())
                .filter(method -> method.isAvailableFor(isInternational, weight, isPerishable))
                .filter(method -> !method.isFree() || orderValue >= 50.00) // Free shipping threshold
                .sorted((a, b) -> Integer.compare(a.deliveryDays, b.deliveryDays))
                .toArray(ShippingMethod[]::new);
    }
    
    public enum SpeedCategory {
        SAME_DAY, OVERNIGHT, EXPRESS, STANDARD, ECONOMY
    }
    
    public enum ServiceLevel {
        ECONOMY, STANDARD, EXPRESS, PREMIUM
    }
}