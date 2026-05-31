package com.gogidix.shared.warehousing.pricing.domain.repository;

import com.gogidix.shared.warehousing.pricing.domain.entity.PriceQuote;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceQuote.QuoteStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Price Quote Repository
 *
 * MongoDB repository for price quotes with multi-tenant support
 */
@Repository
public interface PriceQuoteRepository extends MongoRepository<PriceQuote, String> {

    /**
     * Find quote by tenant and quote number
     */
    Optional<PriceQuote> findByTenantIdAndQuoteNumber(String tenantId, String quoteNumber);

    /**
     * Find quotes by tenant and request ID
     */
    List<PriceQuote> findByTenantIdAndRequestId(String tenantId, String requestId);

    /**
     * Find quotes by tenant and status
     */
    List<PriceQuote> findByTenantIdAndStatus(String tenantId, QuoteStatus status);

    /**
     * Find valid quotes for tenant (not expired)
     */
    List<PriceQuote> findByTenantIdAndValidUntilAfterAndStatusNot(
            String tenantId, LocalDateTime validUntil, QuoteStatus expiredStatus);

    /**
     * Find expired quotes
     */
    List<PriceQuote> findByTenantIdAndValidUntilBeforeAndStatus(
            String tenantId, LocalDateTime validUntil, QuoteStatus status);

    /**
     * Find quotes by tenant
     */
    List<PriceQuote> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
