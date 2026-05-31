package com.gogidix.shared.warehousing.cyclecounting.application.dto;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CountDiscrepancy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Count Discrepancy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountDiscrepancyDTO {

    private String id;
    private String tenantId;
    private String cycleCountId;
    private String countSessionId;
    private String sku;
    private String itemName;
    private String location;
    private CountDiscrepancy.DiscrepancyType discrepancyType;
    private Integer systemQuantity;
    private Integer countedQuantity;
    private Integer variance;
    private BigDecimal unitValue;
    private BigDecimal totalValue;
    private CountDiscrepancy.DiscrepancyStatus status;
    private Boolean resolved;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
    private CountDiscrepancy.AdjustmentAction adjustmentAction;
    private String referenceType;
    private String referenceId;
    private LocalDateTime createdAt;
}
