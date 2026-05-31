package com.gogidix.centralconfiguration.featureflagservice.domain.model;

/**
 * Rollout Strategy enumeration for feature flags.
 * Defines how feature flags are rolled out to users.
 */
public enum RolloutStrategy {
    ALL_USERS("all", "Roll out to all users"),
    PERCENTAGE("percentage", "Roll out to a percentage of users"),
    WHITELIST("whitelist", "Roll out to whitelisted users only"),
    GRADUAL("gradual", "Gradually roll out over time"),
    BETA_TESTERS("beta", "Roll out to beta testers only"),
    INTERNAL("internal", "Roll out to internal users only");

    private final String code;
    private final String description;

    RolloutStrategy(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
