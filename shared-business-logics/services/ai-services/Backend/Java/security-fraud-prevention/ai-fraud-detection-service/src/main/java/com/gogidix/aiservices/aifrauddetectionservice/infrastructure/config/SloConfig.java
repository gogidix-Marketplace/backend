package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

/**
 * Service Level Objective (SLO) Configuration.
 * Defines performance and reliability targets for the fraud detection service.
 */
@Configuration
@PropertySource(value = "classpath:slo-config.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "slo")
public class SloConfig {

    private List<SloDefinition> slo;

    public List<SloDefinition> getSlo() {
        return slo;
    }

    public void setSlo(List<SloDefinition> slo) {
        this.slo = slo;
    }

    public SloDefinition getSloByName(String name) {
        return slo.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get latency threshold for a specific SLO
     */
    public long getLatencyThreshold(String sloName, String thresholdType) {
        SloDefinition sloDef = getSloByName(sloName);
        if (sloDef != null && sloDef.getThresholds() != null) {
            Map<String, Object> thresholds = sloDef.getThresholds();
            Object value = thresholds.get(thresholdType + "_ms");
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        }
        return 1000L; // Default threshold
    }

    /**
     * Check if a given latency meets SLO
     */
    public boolean meetsSlo(String sloName, long latencyMs) {
        SloDefinition sloDef = getSloByName(sloName);
        if (sloDef != null && sloDef.getObjective() != null) {
            Object target = sloDef.getObjective().get("target_latency_ms");
            if (target instanceof Number) {
                return latencyMs <= ((Number) target).longValue();
            }
        }
        return true;
    }

    public static class SloDefinition {
        private String name;
        private String description;
        private Map<String, Object> objective;
        private Map<String, Object> thresholds;
        private List<String> tags;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Map<String, Object> getObjective() {
            return objective;
        }

        public void setObjective(Map<String, Object> objective) {
            this.objective = objective;
        }

        public Map<String, Object> getThresholds() {
            return thresholds;
        }

        public void setThresholds(Map<String, Object> thresholds) {
            this.thresholds = thresholds;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
