package com.gogidix.courier.dynamicpricingservice.domain.repository;

import com.gogidix.courier.dynamicpricingservice.domain.entity.PricingAdjustment;

import java.util.List;
import java.util.Optional;

public interface PricingAdjustmentRepository {

    PricingAdjustment save(PricingAdjustment adjustment);

    Optional<PricingAdjustment> findById(String id);

    List<PricingAdjustment> findByTenantId(String tenantId);

    List<PricingAdjustment> findActiveByTenantId(String tenantId);

    List<PricingAdjustment> findByZoneId(String zoneId);

    void deleteById(String id);
}
