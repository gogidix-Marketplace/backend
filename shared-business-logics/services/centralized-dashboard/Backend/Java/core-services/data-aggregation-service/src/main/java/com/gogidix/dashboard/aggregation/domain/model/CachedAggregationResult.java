package com.gogidix.dashboard.aggregation.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cached Aggregation Result entity.
 * Stores cached results of aggregation queries for performance.
 */
@Entity
@Table(name = "cached_aggregation_results", indexes = {
    @Index(name = "idx_cache_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_cache_key", columnList = "cache_key"),
    @Index(name = "idx_cache_expires_at", columnList = "expires_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedAggregationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "cache_key", nullable = false, length = 500)
    private String cacheKey;

    @Column(name = "result", columnDefinition = "TEXT", nullable = false)
    private String result;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
