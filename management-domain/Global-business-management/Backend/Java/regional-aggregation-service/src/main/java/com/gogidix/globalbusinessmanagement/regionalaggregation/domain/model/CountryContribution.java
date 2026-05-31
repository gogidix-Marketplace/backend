package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing a country's contribution to regional aggregates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "country_contributions")
@CompoundIndex(name = "country_region_period_idx", def = "{'countryCode': 1, 'regionCode': 1, 'periodId': 1}", unique = true)
public class CountryContribution {

    @Id
    private String id;

    @NotBlank(message = "Country code is required")
    @Indexed
    private String countryCode;

    @NotBlank(message = "Country name is required")
    private String countryName;

    @NotBlank(message = "Region code is required")
    @Indexed
    private String regionCode;

    @NotBlank(message = "Region name is required")
    private String regionName;

    @NotBlank(message = "Period identifier is required")
    @Indexed
    private String periodId;

    @NotNull(message = "Period start is required")
    private LocalDateTime periodStart;

    @NotNull(message = "Period end is required")
    private LocalDateTime periodEnd;

    @NotNull(message = "Revenue contribution is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Revenue must be non-negative")
    @Indexed
    private BigDecimal revenueContribution;

    @NotNull(message = "Revenue percentage is required")
    private BigDecimal revenuePercentage;

    @NotNull(message = "Order count is required")
    @Positive(message = "Order count must be positive")
    @Indexed
    private Long orderCount;

    @NotNull(message = "Customer count is required")
    @Positive(message = "Customer count must be positive")
    @Indexed
    private Long customerCount;

    private Long newCustomers;

    private Long churnedCustomers;

    @NotNull(message = "Growth rate is required")
    private BigDecimal growthRate;

    @NotNull(message = "Profit margin is required")
    private BigDecimal profitMargin;

    @Indexed
    private BigDecimal marketShare;

    @Indexed
    private BigDecimal marketPenetration;

    private BigDecimal averageOrderValue;

    private BigDecimal customerLifetimeValue;

    private BigDecimal customerAcquisitionCost;

    @Indexed
    private Integer revenueRank;

    @Indexed
    private Integer growthRank;

    @Indexed
    private Integer profitabilityRank;

    @Indexed
    private Integer overallRank;

    private BigDecimal contributionWeight;

    private BigDecimal performanceScore;

    private BigDecimal efficiencyScore;

    private BigDecimal qualityScore;

    private String trendDirection;

    private BigDecimal changeFromPreviousPeriod;

    private BigDecimal changePercentageFromPreviousPeriod;

    private ContributionStatus status;

    private String notes;

    private Map<String, Object> additionalMetrics;

    @Indexed
    private Instant calculatedAt;

    private Instant createdAt;

    private Instant updatedAt;

    public enum ContributionStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        RECALCULATING
    }

    public boolean isActive() {
        return ContributionStatus.ACTIVE.equals(status);
    }

    public boolean isTopPerformer(Integer threshold) {
        return overallRank != null && overallRank <= threshold;
    }

    public boolean hasPositiveGrowth() {
        return growthRate != null && growthRate.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasHighProfitMargin(BigDecimal threshold) {
        return profitMargin != null && profitMargin.compareTo(threshold) >= 0;
    }

    public BigDecimal calculateRevenuePerCustomer() {
        if (customerCount == null || customerCount == 0 || revenueContribution == null) {
            return BigDecimal.ZERO;
        }
        return revenueContribution.divide(BigDecimal.valueOf(customerCount), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateOrdersPerCustomer() {
        if (customerCount == null || customerCount == 0 || orderCount == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) orderCount / customerCount);
    }

    public BigDecimal calculateCustomerGrowthRate() {
        if (customerCount == null || customerCount == 0 || newCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) newCustomers / customerCount * 100);
    }

    public BigDecimal calculateCustomerChurnRate() {
        if (customerCount == null || customerCount == 0 || churnedCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) churnedCustomers / customerCount * 100);
    }

    public BigDecimal calculateLtvToCacRatio() {
        if (customerAcquisitionCost == null || customerAcquisitionCost.compareTo(BigDecimal.ZERO) == 0 ||
            customerLifetimeValue == null) {
            return BigDecimal.ZERO;
        }
        return customerLifetimeValue.divide(customerAcquisitionCost, 2, BigDecimal.ROUND_HALF_UP);
    }
}
