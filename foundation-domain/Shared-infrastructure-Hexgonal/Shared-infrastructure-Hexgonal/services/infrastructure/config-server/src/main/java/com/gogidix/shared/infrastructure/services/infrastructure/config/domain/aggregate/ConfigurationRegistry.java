package com.gogidix.shared.infrastructure.services.infrastructure.config.domain.aggregate;

import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.event.ConfigurationChangedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.event.ConfigurationReloadedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Configuration Registry Aggregate Root.
 * Manages configuration lifecycle and publishes domain events.
 */
public class ConfigurationRegistry {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationRegistry.class);

    // In-memory configuration store (backed by Git in production)
    private final Map<String, Configuration> configurations = new ConcurrentHashMap<>();
    private final Map<String, Configuration> tenantConfigurations = new ConcurrentHashMap<>();

    // Event handlers
    private final Map<String, Consumer<ConfigurationChangedEvent>> configChangedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<ConfigurationReloadedEvent>> configReloadedHandlers = new ConcurrentHashMap<>();

    public ConfigurationRegistry() {
        log.info("ConfigurationRegistry aggregate initialized");
    }

    // Event handler registration
    public void onConfigurationChanged(Consumer<ConfigurationChangedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        configChangedHandlers.put(handlerId, handler);
    }

    public void onConfigurationReloaded(Consumer<ConfigurationReloadedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        configReloadedHandlers.put(handlerId, handler);
    }

    // Configuration operations
    public Configuration registerConfiguration(String applicationName, String profile, String label,
                                               Map<String, Object> properties, String createdBy) {
        String configId = generateConfigId();
        Instant now = Instant.now();
        String version = "v1";

        Configuration configuration = Configuration.builder()
            .configId(configId)
            .applicationName(applicationName)
            .profile(profile != null ? profile : "default")
            .label(label != null ? label : "main")
            .properties(properties)
            .version(version)
            .createdAt(now)
            .updatedAt(now)
            .createdBy(createdBy)
            .updatedBy(createdBy)
            .build();

        configurations.put(configuration.getCompositeKey(), configuration);

        ConfigurationChangedEvent event = new ConfigurationChangedEvent(
            configId,
            applicationName,
            profile,
            label,
            version,
            "CREATED",
            now
        );
        publishEvent(event);

        log.info("Configuration registered: configId={}, application={}, profile={}",
            configId, applicationName, profile);
        return configuration;
    }

    public Optional<Configuration> updateConfiguration(String configId, Map<String, Object> newProperties,
                                                       String updatedBy) {
        Configuration existing = findConfigurationById(configId).orElse(null);
        if (existing == null) {
            log.warn("Configuration not found for update: configId={}", configId);
            return Optional.empty();
        }

        // Increment version
        String newVersion = incrementVersion(existing.getVersion());

        Configuration updated = Configuration.builder()
            .configId(existing.getConfigId())
            .applicationName(existing.getApplicationName())
            .profile(existing.getProfile())
            .label(existing.getLabel())
            .properties(newProperties)
            .version(newVersion)
            .createdAt(existing.getCreatedAt())
            .updatedAt(Instant.now())
            .createdBy(existing.getCreatedBy())
            .updatedBy(updatedBy)
            .build();

        configurations.put(updated.getCompositeKey(), updated);

        ConfigurationChangedEvent event = new ConfigurationChangedEvent(
            configId,
            existing.getApplicationName(),
            existing.getProfile(),
            existing.getLabel(),
            newVersion,
            "UPDATED",
            Instant.now()
        );
        publishEvent(event);

        log.info("Configuration updated: configId={}, version={}", configId, newVersion);
        return Optional.of(updated);
    }

    public boolean deleteConfiguration(String configId) {
        Configuration existing = findConfigurationById(configId).orElse(null);
        if (existing == null) {
            log.warn("Configuration not found for deletion: configId={}", configId);
            return false;
        }

        configurations.remove(existing.getCompositeKey());

        ConfigurationChangedEvent event = new ConfigurationChangedEvent(
            configId,
            existing.getApplicationName(),
            existing.getProfile(),
            existing.getLabel(),
            existing.getVersion(),
            "DELETED",
            Instant.now()
        );
        publishEvent(event);

        log.info("Configuration deleted: configId={}", configId);
        return true;
    }

    public Configuration registerTenantConfiguration(String tenantId, String applicationName,
                                                     Map<String, Object> properties, String createdBy) {
        String configId = generateConfigId();
        Instant now = Instant.now();

        Configuration configuration = Configuration.builder()
            .configId(configId)
            .applicationName(applicationName)
            .profile("tenant-" + tenantId)
            .label("main")
            .properties(properties)
            .version("v1")
            .createdAt(now)
            .updatedAt(now)
            .createdBy(createdBy)
            .updatedBy(createdBy)
            .build();

        String key = tenantId + ":" + applicationName;
        tenantConfigurations.put(key, configuration);

        ConfigurationChangedEvent event = new ConfigurationChangedEvent(
            configId,
            applicationName,
            "tenant-" + tenantId,
            "main",
            "v1",
            "TENANT_CREATED",
            now
        );
        publishEvent(event);

        log.info("Tenant configuration registered: tenantId={}, application={}", tenantId, applicationName);
        return configuration;
    }

    public Optional<Configuration> getConfiguration(String applicationName, String profile, String label) {
        String key = applicationName + "-" + profile + "-" + label;
        return Optional.ofNullable(configurations.get(key));
    }

    public Optional<Configuration> getTenantConfiguration(String tenantId, String applicationName) {
        String key = tenantId + ":" + applicationName;
        return Optional.ofNullable(tenantConfigurations.get(key));
    }

    public Optional<Configuration> findConfigurationById(String configId) {
        return configurations.values().stream()
            .filter(c -> c.getConfigId().equals(configId))
            .findFirst();
    }

    public List<Configuration> getAllConfigurations() {
        return new ArrayList<>(configurations.values());
    }

    public List<Configuration> getConfigurationsByApplication(String applicationName) {
        return configurations.values().stream()
            .filter(c -> c.getApplicationName().equals(applicationName))
            .toList();
    }

    public List<Configuration> getConfigurationsByProfile(String profile) {
        return configurations.values().stream()
            .filter(c -> c.getProfile().equals(profile))
            .toList();
    }

    public void reloadAllConfigurations() {
        log.info("Reloading all configurations");

        ConfigurationReloadedEvent event = new ConfigurationReloadedEvent(
            "ALL",
            Instant.now(),
            (int) configurations.size()
        );
        publishEvent(event);

        log.info("All configurations reloaded: count={}", configurations.size());
    }

    public void reloadApplicationConfiguration(String applicationName) {
        log.info("Reloading configuration for application: {}", applicationName);

        ConfigurationReloadedEvent event = new ConfigurationReloadedEvent(
            applicationName,
            Instant.now(),
            (int) configurations.values().stream()
                .filter(c -> c.getApplicationName().equals(applicationName))
                .count()
        );
        publishEvent(event);
    }

    // Utility methods
    private String generateConfigId() {
        return "config-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String incrementVersion(String version) {
        if (version.startsWith("v")) {
            try {
                int num = Integer.parseInt(version.substring(1));
                return "v" + (num + 1);
            } catch (NumberFormatException e) {
                return version + "-updated";
            }
        }
        return version + "-updated";
    }

    private void publishEvent(ConfigurationChangedEvent event) {
        try {
            configChangedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in configuration changed handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish ConfigurationChangedEvent", e);
        }
    }

    private void publishEvent(ConfigurationReloadedEvent event) {
        try {
            configReloadedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in configuration reloaded handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish ConfigurationReloadedEvent", e);
        }
    }
}
