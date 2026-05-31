package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Revenue Forecast Domain Entity
 * Multi-tenant revenue forecasting with confidence levels
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "revenue_forecasts")
public class RevenueForecast extends BaseEntity {

    private String forecastId;

    private String tenantId;

    private String forecastName;

    private String description;

    private ForecastType forecastType;

    private LocalDate forecastDate;

    private YearMonth forecastPeriod;

    private Integer horizonMonths;

    private ForecastStatus status;

    @Builder.Default
    private List<ForecastEntry> forecastEntries = new ArrayList<>();

    @Builder.Default
    private List<ForecastSummary> summaries = new ArrayList<>();

    private String createdBy;

    private String department;

    private String territory;

    private String region;

    private String productId;

    private String customerId;

    private ForecastMethod forecastMethod;

    private String notes;

    private String previousForecastId;

    private BigDecimal variancePercentage;

    private Boolean isApproved;

    private String approvedBy;

    private LocalDate approvedAt;

    public enum ForecastType {
        MONTHLY,
        QUARTERLY,
        ANNUAL,
        PROJECT,
        PRODUCT,
        CUSTOMER,
        TERRITORY,
        ROLLING,
        STRATEGIC
    }

    public enum ForecastStatus {
        DRAFT,
        IN_REVIEW,
        APPROVED,
        REJECTED,
        PUBLISHED,
        ARCHIVED
    }

    public enum ForecastMethod {
        HISTORICAL_AVERAGE,
        WEIGHTED_AVERAGE,
        LINEAR_REGRESSION,
        MOVING_AVERAGE,
        EXPONENTIAL_SMOOTHING,
        SALES_PIPELINE,
        MANUAL,
        AI_PREDICTIVE
    }

    public enum ConfidenceLevel {
        PESSIMISTIC,
        REALISTIC,
        OPTIMISTIC
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastEntry {
        private String entryId;
        private YearMonth period;
        private BigDecimal pessimistic;
        private BigDecimal realistic;
        private BigDecimal optimistic;
        private BigDecimal weighted;
        private String currency;
        private ConfidenceLevel selectedConfidence;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastSummary {
        private String category;
        private String categoryId;
        private BigDecimal pessimisticTotal;
        private BigDecimal realisticTotal;
        private BigDecimal optimisticTotal;
        private BigDecimal weightedTotal;
        private String currency;
        private Integer entryCount;
    }

    /**
     * Creates a new forecast
     */
    public static RevenueForecast create(String tenantId, String forecastName,
                                           ForecastType forecastType, LocalDate forecastDate,
                                           Integer horizonMonths, ForecastMethod forecastMethod,
                                           String createdBy) {
        RevenueForecast forecast = RevenueForecast.builder()
                .tenantId(tenantId)
                .forecastName(forecastName)
                .forecastType(forecastType)
                .forecastDate(forecastDate)
                .forecastPeriod(YearMonth.from(forecastDate))
                .horizonMonths(horizonMonths)
                .status(ForecastStatus.DRAFT)
                .forecastMethod(forecastMethod)
                .createdBy(createdBy)
                .forecastEntries(new ArrayList<>())
                .summaries(new ArrayList<>())
                .isApproved(false)
                .build();

        forecast.generateForecastEntries();
        return forecast;
    }

    /**
     * Generates forecast entries for the horizon
     */
    public void generateForecastEntries() {
        this.forecastEntries.clear();
        YearMonth currentPeriod = this.forecastPeriod;

        for (int i = 0; i < this.horizonMonths; i++) {
            YearMonth period = currentPeriod.plusMonths(i);

            ForecastEntry entry = ForecastEntry.builder()
                    .entryId(java.util.UUID.randomUUID().toString())
                    .period(period)
                    .pessimistic(BigDecimal.ZERO)
                    .realistic(BigDecimal.ZERO)
                    .optimistic(BigDecimal.ZERO)
                    .weighted(BigDecimal.ZERO)
                    .currency("USD")
                    .selectedConfidence(ConfidenceLevel.REALISTIC)
                    .build();

            this.forecastEntries.add(entry);
        }
    }

    /**
     * Sets forecast values for a period
     */
    public void setPeriodForecast(String entryId, BigDecimal pessimistic,
                                    BigDecimal realistic, BigDecimal optimistic) {
        ForecastEntry entry = findEntryById(entryId);
        if (entry == null) {
            throw new IllegalArgumentException("Entry not found: " + entryId);
        }

        entry.setPessimistic(pessimistic != null ? pessimistic : BigDecimal.ZERO);
        entry.setRealistic(realistic != null ? realistic : BigDecimal.ZERO);
        entry.setOptimistic(optimistic != null ? optimistic : BigDecimal.ZERO);

        // Calculate weighted average
        BigDecimal weighted = pessimistic.multiply(new BigDecimal("0.25"))
                .add(realistic.multiply(new BigDecimal("0.5")))
                .add(optimistic.multiply(new BigDecimal("0.25")));
        entry.setWeighted(weighted);
    }

    /**
     * Calculates totals for a confidence level
     */
    public BigDecimal getTotalForConfidence(ConfidenceLevel confidence) {
        return this.forecastEntries.stream()
                .map(entry -> {
                    return switch (confidence) {
                        case PESSIMISTIC -> entry.getPessimistic();
                        case REALISTIC -> entry.getRealistic();
                        case OPTIMISTIC -> entry.getOptimistic();
                    };
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets total weighted forecast
     */
    public BigDecimal getTotalWeighted() {
        return this.forecastEntries.stream()
                .map(ForecastEntry::getWeighted)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Submits forecast for review
     */
    public void submitForReview() {
        if (this.status != ForecastStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft forecasts");
        }
        this.status = ForecastStatus.IN_REVIEW;
    }

    /**
     * Approves the forecast
     */
    public void approve(String approvedBy) {
        if (this.status != ForecastStatus.IN_REVIEW) {
            throw new IllegalStateException("Can only approve forecasts in review");
        }
        this.status = ForecastStatus.APPROVED;
        this.isApproved = true;
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDate.now();
    }

    /**
     * Rejects the forecast
     */
    public void reject(String reason) {
        if (this.status != ForecastStatus.IN_REVIEW) {
            throw new IllegalStateException("Can only reject forecasts in review");
        }
        this.status = ForecastStatus.REJECTED;
        this.notes = reason;
    }

    /**
     * Publishes the forecast
     */
    public void publish() {
        if (this.status != ForecastStatus.APPROVED) {
            throw new IllegalStateException("Can only publish approved forecasts");
        }
        this.status = ForecastStatus.PUBLISHED;
    }

    /**
     * Archives the forecast
     */
    public void archive() {
        if (this.status == ForecastStatus.ARCHIVED) {
            throw new IllegalStateException("Forecast already archived");
        }
        this.status = ForecastStatus.ARCHIVED;
    }

    /**
     * Calculates variance from previous forecast
     */
    public void calculateVariance(RevenueForecast previousForecast) {
        if (previousForecast == null) {
            return;
        }

        BigDecimal currentTotal = this.getTotalWeighted();
        BigDecimal previousTotal = previousForecast.getTotalWeighted();

        if (previousTotal.compareTo(BigDecimal.ZERO) != 0) {
            this.variancePercentage = currentTotal.subtract(previousTotal)
                    .divide(previousTotal, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    /**
     * Gets forecast entries for a specific period range
     */
    public List<ForecastEntry> getEntriesForRange(YearMonth start, YearMonth end) {
        return this.forecastEntries.stream()
                .filter(e -> !e.getPeriod().isBefore(start) && !e.getPeriod().isAfter(end))
                .toList();
    }

    /**
     * Finds entry by ID
     */
    private ForecastEntry findEntryById(String entryId) {
        return this.forecastEntries.stream()
                .filter(e -> e.getEntryId().equals(entryId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a summary entry
     */
    public void addSummary(String category, String categoryId, BigDecimal pessimistic,
                           BigDecimal realistic, BigDecimal optimistic, String currency) {
        ForecastSummary summary = ForecastSummary.builder()
                .category(category)
                .categoryId(categoryId)
                .pessimisticTotal(pessimistic)
                .realisticTotal(realistic)
                .optimisticTotal(optimistic)
                .weightedTotal(pessimistic.multiply(new BigDecimal("0.25"))
                        .add(realistic.multiply(new BigDecimal("0.5")))
                        .add(optimistic.multiply(new BigDecimal("0.25"))))
                .currency(currency)
                .entryCount(1)
                .build();

        this.summaries.add(summary);
    }

    /**
     * Gets total for a specific period
     */
    public BigDecimal getPeriodTotal(YearMonth period, ConfidenceLevel confidence) {
        return this.forecastEntries.stream()
                .filter(e -> e.getPeriod().equals(period))
                .map(entry -> {
                    return switch (confidence) {
                        case PESSIMISTIC -> entry.getPessimistic();
                        case REALISTIC -> entry.getRealistic();
                        case OPTIMISTIC -> entry.getOptimistic();
                    };
                })
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Checks if forecast is modifiable
     */
    public boolean isModifiable() {
        return this.status == ForecastStatus.DRAFT || this.status == ForecastStatus.REJECTED;
    }

    /**
     * Gets forecast period as string
     */
    public String getForecastPeriodString() {
        return this.forecastPeriod != null ? this.forecastPeriod.toString() : "";
    }
}
