package com.gogidix.shared.infrastructure.services.infrastructure.config.config;

import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.aggregate.ConfigurationRegistry;
import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.event.ConfigurationChangedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.event.ConfigurationReloadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for config server domain layer.
 * Creates aggregate roots and wires up event handlers.
 */
@Configuration
public class ConfigDomainConfig {

    private static final Logger log = LoggerFactory.getLogger(ConfigDomainConfig.class);

    @Bean
    public ConfigurationRegistry configurationRegistry() {
        ConfigurationRegistry registry = new ConfigurationRegistry();

        // Register event handlers
        registry.onConfigurationChanged(this::handleConfigurationChanged);
        registry.onConfigurationReloaded(this::handleConfigurationReloaded);

        log.info("ConfigurationRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleConfigurationChanged(ConfigurationChangedEvent event) {
        log.info("Configuration changed: configId={}, application={}, profile={}, version={}, changeType={}",
            event.getConfigId(), event.getApplicationName(), event.getProfile(),
            event.getVersion(), event.getChangeType());
    }

    private void handleConfigurationReloaded(ConfigurationReloadedEvent event) {
        log.info("Configuration reloaded: scope={}, count={}", event.getScope(), event.getConfigurationCount());
    }
}
