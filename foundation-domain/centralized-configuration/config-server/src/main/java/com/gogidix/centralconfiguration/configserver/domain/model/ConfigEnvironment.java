package com.gogidix.centralconfiguration.configserver.domain.model;

/**
 * Configuration Environment enumeration.
 * Defines the standard environments for configuration management.
 */
public enum ConfigEnvironment {
    DEVELOPMENT("dev"),
    STAGING("staging"),
    QA("qa"),
    UAT("uat"),
    PRODUCTION("prod"),
    DR("dr");

    private final String profile;

    ConfigEnvironment(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public static ConfigEnvironment fromProfile(String profile) {
        for (ConfigEnvironment env : values()) {
            if (env.profile.equalsIgnoreCase(profile)) {
                return env;
            }
        }
        throw new IllegalArgumentException("Unknown profile: " + profile);
    }
}
