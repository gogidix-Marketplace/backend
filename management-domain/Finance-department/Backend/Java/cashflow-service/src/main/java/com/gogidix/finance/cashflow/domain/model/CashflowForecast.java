package com.gogidix.finance.cashflow.domain.model;

import com.gogidix.finance.cashflow.domain.event.CashflowForecastGeneratedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Cashflow Forecast Domain Entity
 * Multi-tenant cashflow forecasting with projection scenarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "cashflow_forecasts")
public class CashflowForecast {

    @Id
    private String id;

    @Indexed
    private String forecastId;

    @Indexed
    private String tenantId;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ForecastPeriod period;

    private ForecastScenario scenario;

    private ForecastStatus status;

    private String generatedBy;

    private Instant generatedAt;

    private Instant lastUpdated;

    private BigDecimal totalInflow;

    private BigDecimal totalOutflow;

    private BigDecimal netCashflow;

    private BigDecimal openingBalance;

    private BigDecimal closingBalance;

    private BigDecimal minimumBalance;

    private BigDecimal maximumBalance;

    private LocalDate minimumBalanceDate;

    private LocalDate maximumBalanceDate;

    private Integer version;

    private String parentForecastId;

    private Boolean isBaseline;

    private List<ForecastPeriodData> periodData;

    private List<ForecastVariance> variances;

    private List<String> tags;

    private String notes;

    private ConfidenceLevel confidenceLevel;

    private BigDecimal variancePercentage;

    private Instant createdAt;

    private Instant updatedAt;

    @Builder.Default
    private List<CashflowForecastGeneratedEvent> domainEvents = new ArrayList<>();

    public enum ForecastPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        SEMI_ANNUALLY,
        ANNUALLY
    }

    public enum ForecastScenario {
        BASELINE,
        OPTIMISTIC,
        PESSIMISTIC,
        STRESS_TEST,
        CUSTOM
    }

    public enum ForecastStatus {
        DRAFT,
        GENERATING,
        GENERATED,
        APPROVED,
        REJECTED,
        ARCHIVED
    }

    public enum ConfidenceLevel {
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastPeriodData {
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private BigDecimal openingBalance;
        private BigDecimal inflow;
        private BigDecimal outflow;
        private BigDecimal netCashflow;
        private BigDecimal closingBalance;
        private Integer transactionCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastVariance {
        private String category;
        private BigDecimal forecastedAmount;
        private BigDecimal actualAmount;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
        private LocalDate periodDate;
    }

    /**
     * Creates a new forecast
     */
    public static CashflowForecast create(String tenantId, String name,
                                          LocalDate startDate, LocalDate endDate,
                                          ForecastPeriod period, ForecastScenario scenario,
                                          String generatedBy, BigDecimal openingBalance) {
        CashflowForecast forecast = CashflowForecast.builder()
                .forecastId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .period(period)
                .scenario(scenario)
                .generatedBy(generatedBy)
                .openingBalance(openingBalance)
                .status(ForecastStatus.DRAFT)
                .periodData(new ArrayList<>())
                .variances(new ArrayList<>())
                .tags(new ArrayList<>())
                .confidenceLevel(ConfidenceLevel.MEDIUM)
                .version(1)
                .build();

        forecast.addDomainEvent(CashflowForecastGeneratedEvent.builder()
                .forecastId(forecast.getForecastId())
                .tenantId(tenantId)
                .generatedBy(generatedBy)
                .scenario(scenario.name())
                .startDate(startDate)
                .endDate(endDate)
                .timestamp(Instant.now())
                .eventType("FORECAST_CREATED")
                .build());

        return forecast;
    }

    /**
     * Generates the forecast calculations
     */
    public void generate(List<CashflowItem> cashflowItems) {
        this.status = ForecastStatus.GENERATING;

        BigDecimal totalInflow = BigDecimal.ZERO;
        BigDecimal totalOutflow = BigDecimal.ZERO;
        BigDecimal closingBalance = this.openingBalance;
        BigDecimal minBalance = this.openingBalance;
        BigDecimal maxBalance = this.openingBalance;
        LocalDate minDate = this.startDate;
        LocalDate maxDate = this.startDate;

        for (CashflowItem item : cashflowItems) {
            if (item.getType() == CashflowItem.CashflowType.INFLOW) {
                totalInflow = totalInflow.add(item.getNetAmount() != null ? item.getNetAmount() : item.getAmount());
            } else {
                totalOutflow = totalOutflow.add(item.getNetAmount() != null ? item.getNetAmount() : item.getAmount());
            }
        }

        this.totalInflow = totalInflow;
        this.totalOutflow = totalOutflow;
        this.netCashflow = totalInflow.subtract(totalOutflow);
        this.closingBalance = this.openingBalance.add(this.netCashflow);

        this.minimumBalance = minBalance;
        this.maximumBalance = maxBalance;
        this.minimumBalanceDate = minDate;
        this.maximumBalanceDate = maxDate;

        this.status = ForecastStatus.GENERATED;
        this.generatedAt = Instant.now();
        this.lastUpdated = Instant.now();

        addDomainEvent(CashflowForecastGeneratedEvent.builder()
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .generatedBy(this.generatedBy)
                .scenario(this.scenario.name())
                .startDate(this.startDate)
                .endDate(this.endDate)
                .netCashflow(this.netCashflow)
                .timestamp(Instant.now())
                .eventType("FORECAST_GENERATED")
                .build());
    }

    /**
     * Approves the forecast
     */
    public void approve(String approvedBy) {
        if (this.status != ForecastStatus.GENERATED) {
            throw new IllegalStateException("Can only approve generated forecasts");
        }

        this.status = ForecastStatus.APPROVED;
        this.lastUpdated = Instant.now();

        addDomainEvent(CashflowForecastGeneratedEvent.builder()
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .generatedBy(approvedBy)
                .scenario(this.scenario.name())
                .startDate(this.startDate)
                .endDate(this.endDate)
                .timestamp(Instant.now())
                .eventType("FORECAST_APPROVED")
                .build());
    }

    /**
     * Rejects the forecast
     */
    public void reject(String rejectedBy, String reason) {
        if (this.status != ForecastStatus.GENERATED) {
            throw new IllegalStateException("Can only reject generated forecasts");
        }

        this.status = ForecastStatus.REJECTED;
        this.notes = reason;
        this.lastUpdated = Instant.now();

        addDomainEvent(CashflowForecastGeneratedEvent.builder()
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .generatedBy(rejectedBy)
                .scenario(this.scenario.name())
                .startDate(this.startDate)
                .endDate(this.endDate)
                .timestamp(Instant.now())
                .eventType("FORECAST_REJECTED")
                .build());
    }

    /**
     * Archives the forecast
     */
    public void archive() {
        if (this.status == ForecastStatus.ARCHIVED) {
            throw new IllegalStateException("Forecast is already archived");
        }

        this.status = ForecastStatus.ARCHIVED;
        this.lastUpdated = Instant.now();
    }

    /**
     * Creates a new version of the forecast
     */
    public CashflowForecast createNewVersion(String generatedBy) {
        CashflowForecast newForecast = CashflowForecast.builder()
                .forecastId(java.util.UUID.randomUUID().toString())
                .tenantId(this.tenantId)
                .name(this.name + " (v" + (this.version + 1) + ")")
                .description(this.description)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .period(this.period)
                .scenario(this.scenario)
                .generatedBy(generatedBy)
                .openingBalance(this.openingBalance)
                .status(ForecastStatus.DRAFT)
                .periodData(new ArrayList<>())
                .variances(new ArrayList<>())
                .tags(new ArrayList<>(this.tags))
                .confidenceLevel(this.confidenceLevel)
                .version(this.version + 1)
                .parentForecastId(this.forecastId)
                .build();

        return newForecast;
    }

    /**
     * Calculates variance against actuals
     */
    public void calculateVariance(String category, BigDecimal forecastedAmount, BigDecimal actualAmount) {
        BigDecimal variance = actualAmount.subtract(forecastedAmount);
        BigDecimal variancePercentage = forecastedAmount.compareTo(BigDecimal.ZERO) != 0
                ? variance.divide(forecastedAmount, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;

        ForecastVariance forecastVariance = ForecastVariance.builder()
                .category(category)
                .forecastedAmount(forecastedAmount)
                .actualAmount(actualAmount)
                .variance(variance)
                .variancePercentage(variancePercentage)
                .periodDate(LocalDate.now())
                .build();

        if (this.variances == null) {
            this.variances = new ArrayList<>();
        }
        this.variances.add(forecastVariance);
    }

    /**
     * Sets the confidence level
     */
    public void setConfidence(ConfidenceLevel level, BigDecimal variancePercentage) {
        this.confidenceLevel = level;
        this.variancePercentage = variancePercentage;
    }

    /**
     * Adds a tag to the forecast
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Checks if forecast is positive (cashflow surplus)
     */
    public boolean isPositive() {
        return this.netCashflow != null && this.netCashflow.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if forecast is negative (cashflow deficit)
     */
    public boolean isNegative() {
        return this.netCashflow != null && this.netCashflow.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Gets the forecast duration in days
     */
    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(this.startDate, this.endDate);
    }

    public void addDomainEvent(CashflowForecastGeneratedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
