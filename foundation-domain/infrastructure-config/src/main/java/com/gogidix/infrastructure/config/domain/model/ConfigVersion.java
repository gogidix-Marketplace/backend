package com.gogidix.infrastructure.config.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model for configuration version history.
 *
 * <p>Tracks all changes to configuration properties and feature flags:</p>
 * <ul>
 *   <li>Complete audit trail of changes</li>
 *   <li>Rollback capability to any version</li>
 *   <li>Change comparison and diff support</li>
 *   <li>Author and timestamp tracking</li>
 *   <li>Change reason documentation</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "config_versions")
public class ConfigVersion {

    /**
     * Unique identifier for the version record.
     */
    @Id
    private String id;

    /**
     * Tenant identifier.
     */
    @Indexed
    private String tenantId;

    /**
     * ID of the configuration entity being versioned.
     */
    @Indexed
    private String configId;

    /**
     * Type of configuration being versioned.
     */
    @Indexed
    private ConfigType configType;

    /**
     * Key of the configuration being versioned.
     */
    @Indexed
    private String configKey;

    /**
     * Version number.
     */
    @Indexed
    private Integer version;

    /**
     * Previous value of the configuration.
     */
    private String previousValue;

    /**
     * New value of the configuration.
     */
    private String newValue;

    /**
     * Full snapshot of the configuration at this version.
     */
    private Map<String, Object> snapshot;

    /**
     * Type of change that was made.
     */
    private ChangeType changeType;

    /**
     * User who made the change.
     */
    private String changedBy;

    /**
     * Reason for the change.
     */
    private String changeReason;

    /**
     * Additional metadata about the change.
     */
    private Map<String, Object> metadata;

    /**
     * Timestamp when the change was made.
     */
    private LocalDateTime changedAt;

    /**
     * IP address from which the change was made.
     */
    private String changedFrom;

    /**
     * User agent of the client making the change.
     */
    private String userAgent;

    /**
     * Whether this version can be rolled back to.
     */
    @Builder.Default
    private boolean canRollback = true;

    /**
     * Configuration type enumeration.
     */
    public enum ConfigType {
        CONFIGURATION_PROPERTY,
        FEATURE_FLAG,
        SECRET
    }

    /**
     * Change type enumeration.
     */
    public enum ChangeType {
        CREATED,
        UPDATED,
        DELETED,
        ENABLED,
        DISABLED,
        ROTATED,
        ROLLED_BACK
    }

    /**
     * Creates a diff representation of the change.
     *
     * @return A string representing the diff
     */
    public String getDiff() {
        if (changeType == ChangeType.CREATED) {
            return String.format("Created with value: %s", truncateValue(newValue));
        } else if (changeType == ChangeType.DELETED) {
            return String.format("Deleted (was: %s)", truncateValue(previousValue));
        } else if (changeType == ChangeType.UPDATED) {
            return String.format("Changed from '%s' to '%s'",
                    truncateValue(previousValue), truncateValue(newValue));
        } else if (changeType == ChangeType.ENABLED) {
            return "Enabled";
        } else if (changeType == ChangeType.DISABLED) {
            return "Disabled";
        } else if (changeType == ChangeType.ROTATED) {
            return "Rotated";
        } else if (changeType == ChangeType.ROLLED_BACK) {
            return String.format("Rolled back to version %d", version);
        }
        return changeType.toString();
    }

    /**
     * Truncates a value for display purposes.
     */
    private String truncateValue(String value) {
        if (value == null) {
            return "null";
        }
        int maxLength = 100;
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    /**
     * Checks if this version represents a value change.
     */
    public boolean isValueChange() {
        return changeType == ChangeType.CREATED
                || changeType == ChangeType.UPDATED
                || changeType == ChangeType.DELETED;
    }

    /**
     * Checks if this version represents a state change.
     */
    public boolean isStateChange() {
        return changeType == ChangeType.ENABLED
                || changeType == ChangeType.DISABLED;
    }

    /**
     * Creates a summary of the change.
     */
    public String getSummary() {
        return String.format("%s %s '%s' by %s at %s",
                changeType, configType, configKey, changedBy, changedAt);
    }
}
