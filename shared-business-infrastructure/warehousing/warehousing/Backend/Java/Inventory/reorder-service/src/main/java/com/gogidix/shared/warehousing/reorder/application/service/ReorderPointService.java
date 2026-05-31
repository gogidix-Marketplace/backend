package com.gogidix.shared.warehousing.reorder.application.service;

import com.gogidix.shared.warehousing.reorder.application.dto.ReorderPointDTO;
import com.gogidix.shared.warehousing.reorder.domain.entity.ReorderPoint;
import com.gogidix.shared.warehousing.reorder.domain.exception.ReorderNotFoundException;
import com.gogidix.shared.warehousing.reorder.domain.repository.ReorderPointRepository;
import com.gogidix.shared.warehousing.reorder.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reorder Point Application Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReorderPointService {

    private final ReorderPointRepository reorderPointRepository;

    /**
     * Create a new reorder point
     */
    public ReorderPointDTO createReorderPoint(ReorderPoint reorderPoint) {
        log.info("Creating reorder point for SKU: {}", reorderPoint.getSku());

        String tenantId = TenantContext.getCurrentTenantId();
        reorderPoint.setTenantId(tenantId);

        // Set default values
        if (reorderPoint.getStatus() == null) {
            reorderPoint.setStatus(ReorderPoint.ReorderStatus.ACTIVE);
        }
        if (reorderPoint.getAutoReorderEnabled() == null) {
            reorderPoint.setAutoReorderEnabled(false);
        }
        if (reorderPoint.getCurrentStockLevel() == null) {
            reorderPoint.setCurrentStockLevel(0);
        }

        ReorderPoint saved = reorderPointRepository.save(reorderPoint);
        log.info("Reorder point created with ID: {}", saved.getId());
        return toDTO(saved);
    }

    /**
     * Get reorder point by ID
     */
    @Transactional(readOnly = true)
    public ReorderPointDTO getReorderPoint(String id) {
        ReorderPoint reorderPoint = reorderPointRepository.findById(id)
            .orElseThrow(() -> new ReorderNotFoundException("ReorderPoint", id));
        return toDTO(reorderPoint);
    }

    /**
     * Get reorder point by SKU
     */
    @Transactional(readOnly = true)
    public ReorderPointDTO getReorderPointBySku(String sku) {
        String tenantId = TenantContext.getCurrentTenantId();
        ReorderPoint reorderPoint = reorderPointRepository.findByTenantIdAndSku(tenantId, sku)
            .orElseThrow(() -> new ReorderNotFoundException("ReorderPoint", sku));
        return toDTO(reorderPoint);
    }

    /**
     * Get all reorder points for current tenant
     */
    @Transactional(readOnly = true)
    public List<ReorderPointDTO> getAllReorderPoints() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ReorderPoint> reorderPoints = reorderPointRepository.findByTenantId(tenantId);
        return reorderPoints.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get reorder points by status
     */
    @Transactional(readOnly = true)
    public List<ReorderPointDTO> getReorderPointsByStatus(ReorderPoint.ReorderStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ReorderPoint> reorderPoints = reorderPointRepository.findByTenantIdAndStatus(tenantId, status);
        return reorderPoints.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Check stock levels and update status
     */
    public ReorderPointDTO checkStockLevel(String id, Integer currentStock) {
        log.info("Checking stock level for reorder point: {}", id);

        ReorderPoint reorderPoint = reorderPointRepository.findById(id)
            .orElseThrow(() -> new ReorderNotFoundException("ReorderPoint", id));

        reorderPoint.setCurrentStockLevel(currentStock);
        reorderPoint.setLastStockCheckDate(LocalDateTime.now());

        // Update status based on stock level
        if (currentStock <= reorderPoint.getReorderLevel()) {
            reorderPoint.setStatus(ReorderPoint.ReorderStatus.BELOW_REORDER_POINT);
        } else if (reorderPoint.getMaxStockLevel() != null &&
                   currentStock >= reorderPoint.getMaxStockLevel()) {
            reorderPoint.setStatus(ReorderPoint.ReorderStatus.REORDERED);
        } else {
            reorderPoint.setStatus(ReorderPoint.ReorderStatus.ACTIVE);
        }

        ReorderPoint updated = reorderPointRepository.save(reorderPoint);
        log.info("Stock level checked for: {}, status: {}", id, updated.getStatus());
        return toDTO(updated);
    }

    /**
     * Update reorder point
     */
    public ReorderPointDTO updateReorderPoint(String id, ReorderPoint updates) {
        log.info("Updating reorder point: {}", id);

        ReorderPoint reorderPoint = reorderPointRepository.findById(id)
            .orElseThrow(() -> new ReorderNotFoundException("ReorderPoint", id));

        if (updates.getReorderLevel() != null) {
            reorderPoint.setReorderLevel(updates.getReorderLevel());
        }
        if (updates.getMaxStockLevel() != null) {
            reorderPoint.setMaxStockLevel(updates.getMaxStockLevel());
        }
        if (updates.getEconomicOrderQuantity() != null) {
            reorderPoint.setEconomicOrderQuantity(updates.getEconomicOrderQuantity());
        }
        if (updates.getSafetyStockLevel() != null) {
            reorderPoint.setSafetyStockLevel(updates.getSafetyStockLevel());
        }
        if (updates.getLeadTimeDays() != null) {
            reorderPoint.setLeadTimeDays(updates.getLeadTimeDays());
        }
        if (updates.getReviewPeriodDays() != null) {
            reorderPoint.setReviewPeriodDays(updates.getReviewPeriodDays());
        }
        if (updates.getAutoReorderEnabled() != null) {
            reorderPoint.setAutoReorderEnabled(updates.getAutoReorderEnabled());
        }
        if (updates.getMinimumOrderQuantity() != null) {
            reorderPoint.setMinimumOrderQuantity(updates.getMinimumOrderQuantity());
        }
        if (updates.getOrderMultiple() != null) {
            reorderPoint.setOrderMultiple(updates.getOrderMultiple());
        }
        if (updates.getStandardCost() != null) {
            reorderPoint.setStandardCost(updates.getStandardCost());
        }

        ReorderPoint updated = reorderPointRepository.save(reorderPoint);
        log.info("Reorder point updated: {}", id);
        return toDTO(updated);
    }

    /**
     * Delete reorder point
     */
    public void deleteReorderPoint(String id) {
        log.info("Deleting reorder point: {}", id);

        if (!reorderPointRepository.existsById(id)) {
            throw new ReorderNotFoundException("ReorderPoint", id);
        }

        reorderPointRepository.deleteById(id);
        log.info("Reorder point deleted: {}", id);
    }

    private ReorderPointDTO toDTO(ReorderPoint reorderPoint) {
        return ReorderPointDTO.builder()
            .id(reorderPoint.getId())
            .tenantId(reorderPoint.getTenantId())
            .sku(reorderPoint.getSku())
            .productName(reorderPoint.getProductName())
            .supplierId(reorderPoint.getSupplierId())
            .supplierName(reorderPoint.getSupplierName())
            .reorderLevel(reorderPoint.getReorderLevel())
            .maxStockLevel(reorderPoint.getMaxStockLevel())
            .economicOrderQuantity(reorderPoint.getEconomicOrderQuantity())
            .safetyStockLevel(reorderPoint.getSafetyStockLevel())
            .leadTimeDays(reorderPoint.getLeadTimeDays())
            .reviewPeriodDays(reorderPoint.getReviewPeriodDays())
            .status(reorderPoint.getStatus())
            .currentStockLevel(reorderPoint.getCurrentStockLevel())
            .lastStockCheckDate(reorderPoint.getLastStockCheckDate())
            .lastReorderDate(reorderPoint.getLastReorderDate())
            .autoReorderEnabled(reorderPoint.getAutoReorderEnabled())
            .minimumOrderQuantity(reorderPoint.getMinimumOrderQuantity())
            .orderMultiple(reorderPoint.getOrderMultiple())
            .unitOfMeasure(reorderPoint.getUnitOfMeasure())
            .standardCost(reorderPoint.getStandardCost())
            .currencyCode(reorderPoint.getCurrencyCode())
            .createdAt(reorderPoint.getCreatedAt())
            .updatedAt(reorderPoint.getUpdatedAt())
            .build();
    }
}
