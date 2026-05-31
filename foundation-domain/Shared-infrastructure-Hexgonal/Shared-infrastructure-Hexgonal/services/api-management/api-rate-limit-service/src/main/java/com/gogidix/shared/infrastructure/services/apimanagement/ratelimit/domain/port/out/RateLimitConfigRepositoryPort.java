package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.domain.port.out;

import com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.domain.model.RateLimitConfig;

import java.util.List;
import java.util.Optional;

/**
 * Output port for RateLimitConfig repository
 */
public interface RateLimitConfigRepositoryPort {

    RateLimitConfig save(RateLimitConfig entity);
    Optional<RateLimitConfig> findById(String id);
    List<RateLimitConfig> findAllByTenantId(String tenantId);
    Optional<RateLimitConfig> findByIdAndTenantId(String id, String tenantId);
    List<RateLimitConfig> findByApiKeyAndTenantId(String apiKey, String tenantId);
    List<RateLimitConfig> findByActiveTrueAndTenantId(String tenantId);
    void deleteByIdAndTenantId(String id, String tenantId);
    long countByTenantId(String tenantId);
}
