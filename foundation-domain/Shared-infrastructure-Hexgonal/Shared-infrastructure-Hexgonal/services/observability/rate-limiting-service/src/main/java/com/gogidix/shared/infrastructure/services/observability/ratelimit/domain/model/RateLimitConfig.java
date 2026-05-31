package com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Rate Limit Configuration domain entity.
 * <p>
 * Defines rate limiting rules for APIs and tenants.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rate_limit_configs")
public class RateLimitConfig {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed
    private String apiKey;

    @Indexed
    private String route;

    private RateLimitType type;

    private long capacity; // Maximum requests

    private long refillTokens; // Tokens to refill

    private long refillPeriod; // Refill period in seconds

    private boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Rate limit types.
     */
    public enum RateLimitType {
        API_KEY,
        USER,
        TENANT,
        IP_ADDRESS,
        GLOBAL
    }

    /**
     * Calculates the refill rate in tokens per second.
     *
     * @return tokens per second
     */
    public double getRefillRatePerSecond() {
        return (double) refillTokens / refillPeriod;
    }
}
