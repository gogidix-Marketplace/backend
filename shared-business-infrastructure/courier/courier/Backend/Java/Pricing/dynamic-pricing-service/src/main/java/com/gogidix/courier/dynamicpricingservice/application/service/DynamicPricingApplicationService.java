package com.gogidix.courier.dynamicpricingservice.application.service;

import com.gogidix.courier.dynamicpricingservice.application.dto.*;
import com.gogidix.courier.dynamicpricingservice.domain.entity.DemandLevel;
import com.gogidix.courier.dynamicpricingservice.domain.entity.SurgeMultiplier;
import com.gogidix.courier.dynamicpricingservice.domain.repository.DemandLevelRepository;
import com.gogidix.courier.dynamicpricingservice.domain.repository.SurgeMultiplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DynamicPricingApplicationService {

    private static final Logger log = LoggerFactory.getLogger(DynamicPricingApplicationService.class);
    private static final double SURGE_THRESHOLD = 1.5;
    private static final double HIGH_SURGE_THRESHOLD = 2.0;
    private static final BigDecimal MAX_MULTIPLIER = BigDecimal.valueOf(3.0);

    private final SurgeMultiplierRepository surgeRepository;
    private final DemandLevelRepository demandRepository;

    public DynamicPricingApplicationService(
            SurgeMultiplierRepository surgeRepository,
            DemandLevelRepository demandRepository) {
        this.surgeRepository = surgeRepository;
        this.demandRepository = demandRepository;
    }

    @Cacheable(value = "surgeStatus", key = "#zoneId")
    public SurgeStatusResponse getSurgeStatus(String zoneId) {
        log.debug("Getting surge status for zone: {}", zoneId);

        SurgeMultiplier surge = surgeRepository.findActiveByZoneId(zoneId).orElse(null);

        if (surge == null) {
            return new SurgeStatusResponse(
                    zoneId,
                    BigDecimal.ONE,
                    SurgeMultiplier.DemandLevel.NORMAL,
                    null,
                    null,
                    false
            );
        }

        return new SurgeStatusResponse(
                surge.getZoneId(),
                surge.getMultiplier(),
                surge.getDemandLevel(),
                surge.getEffectiveTime(),
                surge.getExpiryTime(),
                surge.isActive()
        );
    }

    public SurgePriceResponse calculateSurgePrice(String tenantId, CalculateSurgeRequest request) {
        log.debug("Calculating surge price for zone: {}", request.zoneId());

        SurgeMultiplier surge = surgeRepository.findActiveByZoneId(request.zoneId()).orElse(null);

        BigDecimal multiplier = BigDecimal.ONE;
        String demandLevel = "NORMAL";

        if (surge != null && surge.isActive()) {
            multiplier = surge.getMultiplier();
            demandLevel = surge.getDemandLevel().name();
        }

        BigDecimal surgeAmount = request.basePrice()
                .multiply(multiplier.subtract(BigDecimal.ONE))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalPrice = request.basePrice()
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);

        return new SurgePriceResponse(
                request.basePrice(),
                multiplier,
                surgeAmount,
                totalPrice,
                demandLevel
        );
    }

    @Transactional
    @CacheEvict(value = "surgeStatus", allEntries = true)
    public void updateDemandLevel(String tenantId, UpdateDemandRequest request) {
        log.debug("Updating demand level for zone: {} - orders: {}, drivers: {}",
                request.zoneId(), request.activeOrders(), request.availableDrivers());

        DemandLevel.Level level = calculateDemandLevel(
                request.activeOrders(),
                request.availableDrivers()
        );

        DemandLevel demandLevel = new DemandLevel(
                tenantId,
                request.zoneId(),
                level,
                request.activeOrders(),
                request.availableDrivers()
        );

        demandRepository.save(demandLevel);

        // Auto-adjust surge based on demand
        autoAdjustSurge(tenantId, request.zoneId(), level, demandLevel.getRatio());
    }

    private void autoAdjustSurge(String tenantId, String zoneId, DemandLevel.Level level, Double ratio) {
        BigDecimal multiplier = BigDecimal.ONE;
        SurgeMultiplier.DemandLevel surgeLevel = SurgeMultiplier.DemandLevel.NORMAL;

        switch (level) {
            case ELEVATED:
                multiplier = BigDecimal.valueOf(1.2);
                surgeLevel = SurgeMultiplier.DemandLevel.NORMAL;
                break;
            case HIGH:
                multiplier = BigDecimal.valueOf(1.5);
                surgeLevel = SurgeMultiplier.DemandLevel.HIGH;
                break;
            case SURGE:
                multiplier = BigDecimal.valueOf(Math.min(2.0, ratio));
                surgeLevel = SurgeMultiplier.DemandLevel.CRITICAL;
                break;
            default:
                // No surge for LOW and NORMAL
                break;
        }

        // Cap at max multiplier
        if (multiplier.compareTo(MAX_MULTIPLIER) > 0) {
            multiplier = MAX_MULTIPLIER;
        }

        if (multiplier.compareTo(BigDecimal.ONE) > 0) {
            SurgeMultiplier surge = new SurgeMultiplier(tenantId, zoneId, multiplier, surgeLevel);
            surge.setExpiryTime(LocalDateTime.now().plusHours(1));
            surgeRepository.save(surge);
            log.info("Applied surge multiplier {} for zone {}", multiplier, zoneId);
        } else {
            // Clear existing surge
            surgeRepository.findActiveByZoneId(zoneId).ifPresent(existing -> {
                existing.setExpiryTime(LocalDateTime.now());
                surgeRepository.save(existing);
            });
        }
    }

    private DemandLevel.Level calculateDemandLevel(int activeOrders, int availableDrivers) {
        if (availableDrivers == 0) {
            return DemandLevel.Level.SURGE;
        }

        double ratio = (double) activeOrders / availableDrivers;

        if (ratio < 0.5) {
            return DemandLevel.Level.LOW;
        } else if (ratio < 1.0) {
            return DemandLevel.Level.NORMAL;
        } else if (ratio < 1.5) {
            return DemandLevel.Level.ELEVATED;
        } else if (ratio < 2.0) {
            return DemandLevel.Level.HIGH;
        } else {
            return DemandLevel.Level.SURGE;
        }
    }
}
