package com.gogidix.shared.courier.pricing.domain.repository;

import com.gogidix.shared.courier.pricing.domain.entity.PriceQuote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Price Quote Entity
 * Manages temporary price quotes
 */
@Repository
public interface PriceQuoteRepository extends MongoRepository<PriceQuote, String> {

    /**
     * Find quote by tenant and quote ID
     */
    Optional<PriceQuote> findByTenantIdAndQuoteId(String tenantId, String quoteId);

    /**
     * Find quotes by customer
     */
    List<PriceQuote> findByTenantIdAndCustomerId(String tenantId, String customerId);

    /**
     * Find active (not expired, not accepted) quotes
     */
    List<PriceQuote> findByTenantIdAndExpiredFalseAndAcceptedFalse(String tenantId);

    /**
     * Find expired quotes
     */
    List<PriceQuote> findByTenantIdAndExpiresAtBefore(String tenantId, LocalDateTime now);

    /**
     * Delete expired quotes
     */
    void deleteByTenantIdAndExpiresAtBefore(String tenantId, LocalDateTime now);
}
