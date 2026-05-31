package com.gogidix.shared.infrastructure.services.gateway.discovery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuration for Eureka Server with CORS and cluster support.
 */
@Configuration
public class EurekaServerConfig {

    @Autowired
    private Environment environment;

    /**
     * Configure CORS filter for Eureka Dashboard access.
     */
    @Bean
    @ConditionalOnProperty(name = "eureka.dashboard.cors.enabled", havingValue = "true", matchIfMissing = false)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Tenant-ID"));
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * Check if self-preservation should be enabled.
     */
    public boolean shouldEnableSelfPreservation() {
        return environment.getProperty("eureka.server.enable-self-preservation", Boolean.class, true);
    }

    /**
     * Get the renewal threshold.
     */
    public double getRenewalThreshold() {
        return environment.getProperty("eureka.server.renewal-percent-threshold", Double.class, 0.85);
    }
}
