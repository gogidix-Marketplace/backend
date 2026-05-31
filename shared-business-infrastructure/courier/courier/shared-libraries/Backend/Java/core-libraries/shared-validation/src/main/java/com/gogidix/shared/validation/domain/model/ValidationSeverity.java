package com.gogidix.shared.validation.domain.model;

/**
 * Enumeration of validation severity levels.
 */
public enum ValidationSeverity {
    
    INFO("Info", 1, "Informational message"),
    WARNING("Warning", 2, "Warning - validation failed but not critical"),
    ERROR("Error", 3, "Error - validation failed and must be addressed"),
    CRITICAL("Critical", 4, "Critical error - validation failed with severe consequences");
    
    private final String displayName;
    private final int level;
    private final String description;
    
    ValidationSeverity(String displayName, int level, String description) {
        this.displayName = displayName;
        this.level = level;
        this.description = description;
    }
    
    /**
     * Gets the display name of the severity level.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the numeric level of the severity.
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Gets the description of the severity level.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this severity is more severe than another.
     */
    public boolean isMoreSevereThan(ValidationSeverity other) {
        return this.level > other.level;
    }
    
    /**
     * Checks if this severity is at least as severe as another.
     */
    public boolean isAtLeastAsSevereAs(ValidationSeverity other) {
        return this.level >= other.level;
    }
    
    /**
     * Checks if this severity indicates a blocking error.
     */
    public boolean isBlocking() {
        return this == ERROR || this == CRITICAL;
    }
    
    /**
     * Gets the most severe severity from a collection.
     */
    public static ValidationSeverity getMostSevere(ValidationSeverity... severities) {
        ValidationSeverity mostSevere = INFO;
        for (ValidationSeverity severity : severities) {
            if (severity.isMoreSevereThan(mostSevere)) {
                mostSevere = severity;
            }
        }
        return mostSevere;
    }
    
    /**
     * Gets appropriate color for UI representation.
     */
    public String getColorCode() {
        return switch (this) {
            case INFO -> "#2196F3";      // Blue
            case WARNING -> "#FF9800";   // Orange
            case ERROR -> "#F44336";     // Red
            case CRITICAL -> "#9C27B0";  // Purple
        };
    }
    
    /**
     * Gets appropriate icon for UI representation.
     */
    public String getIcon() {
        return switch (this) {
            case INFO -> "info";
            case WARNING -> "warning";
            case ERROR -> "error";
            case CRITICAL -> "error_outline";
        };
    }
}