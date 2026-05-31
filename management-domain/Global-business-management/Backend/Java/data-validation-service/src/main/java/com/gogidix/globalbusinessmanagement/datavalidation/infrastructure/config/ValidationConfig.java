package com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for validation service
 */
@Configuration
@ConfigurationProperties(prefix = "validation")
public class ValidationConfig {

    private Rules rules = new Rules();
    private Quality quality = new Quality();
    private Batch batch = new Batch();

    public static class Rules {
        private boolean enabled = true;
        private int cacheSize = 1000;
        private int cacheTtlMinutes = 60;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getCacheSize() { return cacheSize; }
        public void setCacheSize(int cacheSize) { this.cacheSize = cacheSize; }

        public int getCacheTtlMinutes() { return cacheTtlMinutes; }
        public void setCacheTtlMinutes(int cacheTtlMinutes) { this.cacheTtlMinutes = cacheTtlMinutes; }
    }

    public static class Quality {
        private int excellentThreshold = 95;
        private int goodThreshold = 80;
        private int acceptableThreshold = 60;

        public int getExcellentThreshold() { return excellentThreshold; }
        public void setExcellentThreshold(int excellentThreshold) { this.excellentThreshold = excellentThreshold; }

        public int getGoodThreshold() { return goodThreshold; }
        public void setGoodThreshold(int goodThreshold) { this.goodThreshold = goodThreshold; }

        public int getAcceptableThreshold() { return acceptableThreshold; }
        public void setAcceptableThreshold(int acceptableThreshold) { this.acceptableThreshold = acceptableThreshold; }
    }

    public static class Batch {
        private int maxBatchSize = 1000;
        private int timeoutSeconds = 300;

        public int getMaxBatchSize() { return maxBatchSize; }
        public void setMaxBatchSize(int maxBatchSize) { this.maxBatchSize = maxBatchSize; }

        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    }

    public Rules getRules() { return rules; }
    public void setRules(Rules rules) { this.rules = rules; }

    public Quality getQuality() { return quality; }
    public void setQuality(Quality quality) { this.quality = quality; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
}
