package com.gogidix.management.executive.strategy.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Data Feed domain model
 * Represents an external data feed configuration for strategy widgets
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "data_feeds")
public class DataFeed extends BaseEntity {

    private String name;
    private String description;
    private String feedType;
    private String endpoint;
    private FeedConfig config;
    private FeedStatus status;
    private Instant lastFetchAt;
    private Instant nextFetchAt;
    private String errorMessage;

    public enum FeedStatus {
        ACTIVE, INACTIVE, ERROR, PAUSED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedConfig {
        private String url;
        private String method;
        private Map<String, String> headers;
        private String authType;
        private String authToken;
        private int refreshInterval; // in seconds
        private Map<String, Object> queryParams;
    }
}
