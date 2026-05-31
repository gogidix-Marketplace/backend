package com.gogidix.finance.forecasting.domain.model;

import com.gogidix.finance.forecasting.domain.event.ForecastApprovedEvent;
import com.gogidix.finance.forecasting.domain.event.ForecastGeneratedEvent;
import com.gogidix.finance.forecasting.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Forecast Domain Entity
 * Multi-tenant financial forecasting with various forecast types and horizons
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "forecasts")
public class Forecast extends BaseEntity {

    private String forecastId;

    private String tenantId;

    private ForecastType forecastType;

    private ForecastHorizon forecastHorizon;

    private String name;

    private String description;

    private Instant startDate;

    private Instant endDate;

    private ForecastStatus status;

    private String createdBy;

    private String approvedBy;

    private Instant approvedAt;

    private String rejectionReason;

    private String currency;

    private BigDecimal totalForecastAmount;

    private BigDecimal actualAmount;

    private BigDecimal varianceAmount;

    private BigDecimal variancePercentage;

    private Integer confidenceLevel;

    private String dataSource;

    private String department;

    private String category;

    private String scenario;

    private List<ForecastMetric> metrics;

    private String notes;

    private Instant lastRegeneratedAt;

    private Integer regenerationCount;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    /**
     * Forecast Type Enum
     * Defines the different types of financial forecasts
     */
    public enum ForecastType {
        REVENUE,
        EXPENSE,
        CASHFLOW,
        BUDGET_VARIANCE
    }

    /**
     * Forecast Horizon Enum
     * Defines the time period for forecasts
     */
    public enum ForecastHorizon {
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }

    /**
     * Forecast Status Enum
     * Defines the lifecycle states of a forecast
     */
    public enum ForecastStatus {
        DRAFT,
        PENDING_APPROVAL,
        APPROVED,
        REJECTED,
        ARCHIVED
    }

    /**
     * Creates a new forecast
     *
     * @param tenantId the tenant identifier
     * @param name the forecast name
     * @param forecastType the type of forecast
     * @param forecastHorizon the forecast horizon
     * @param startDate the forecast start date
     * @param endDate the forecast end date
     * @param currency the currency code
     * @param createdBy the user creating the forecast
     * @return a new Forecast instance
     */
    public static Forecast create(String tenantId, String name, ForecastType forecastType,
                                  ForecastHorizon forecastHorizon, Instant startDate, Instant endDate,
                                  String currency, String createdBy) {
        Forecast forecast = Forecast.builder()
                .forecastId(generateForecastId())
                .tenantId(tenantId)
                .name(name)
                .forecastType(forecastType)
                .forecastHorizon(forecastHorizon)
                .startDate(startDate)
                .endDate(endDate)
                .currency(currency)
                .createdBy(createdBy)
                .status(ForecastStatus.DRAFT)
                .confidenceLevel(75)
                .metrics(new ArrayList<>())
                .regenerationCount(0)
                .build();

        forecast.addDomainEvent(ForecastGeneratedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .forecastId(forecast.getForecastId())
                .tenantId(tenantId)
                .forecastType(forecastType.name())
                .forecastHorizon(forecastHorizon.name())
                .timestamp(Instant.now())
                .eventType("FORECAST_CREATED")
                .build());

        return forecast;
    }

    /**
     * Submits the forecast for approval
     *
     * @throws IllegalStateException if forecast is not in DRAFT status
     */
    public void submitForApproval() {
        if (this.status != ForecastStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft forecasts for approval");
        }

        validateForecast();

        this.status = ForecastStatus.PENDING_APPROVAL;

        addDomainEvent(ForecastGeneratedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .forecastType(this.forecastType.name())
                .forecastHorizon(this.forecastHorizon.name())
                .timestamp(Instant.now())
                .eventType("FORECAST_SUBMITTED")
                .build());
    }

    /**
     * Approves the forecast
     *
     * @param approver the user approving the forecast
     * @throws IllegalStateException if forecast is not pending approval
     */
    public void approve(String approver) {
        if (this.status != ForecastStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only approve forecasts pending approval");
        }

        this.status = ForecastStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = Instant.now();

        addDomainEvent(ForecastApprovedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .approvedBy(approver)
                .forecastType(this.forecastType.name())
                .timestamp(Instant.now())
                .eventType("FORECAST_APPROVED")
                .build());
    }

    /**
     * Rejects the forecast
     *
     * @param approver the user rejecting the forecast
     * @param reason the rejection reason
     * @throws IllegalStateException if forecast is not pending approval
     */
    public void reject(String approver, String reason) {
        if (this.status != ForecastStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only reject forecasts pending approval");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }

        this.status = ForecastStatus.REJECTED;
        this.approvedBy = approver;
        this.rejectionReason = reason;
        this.approvedAt = Instant.now();

        addDomainEvent(ForecastGeneratedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .forecastType(this.forecastType.name())
                .timestamp(Instant.now())
                .eventType("FORECAST_REJECTED")
                .build());
    }

    /**
     * Regenerates the forecast with new data
     *
     * @param newMetrics the new forecast metrics
     * @param newTotalAmount the new total forecast amount
     * @throws IllegalStateException if forecast is approved
     */
    public void regenerate(List<ForecastMetric> newMetrics, BigDecimal newTotalAmount) {
        if (this.status == ForecastStatus.APPROVED) {
            throw new IllegalStateException("Cannot regenerate approved forecasts. Create a new version instead.");
        }

        this.metrics = newMetrics != null ? newMetrics : new ArrayList<>();
        this.totalForecastAmount = newTotalAmount;
        this.lastRegeneratedAt = Instant.now();
        this.regenerationCount = (this.regenerationCount != null ? this.regenerationCount : 0) + 1;

        addDomainEvent(ForecastGeneratedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .forecastType(this.forecastType.name())
                .timestamp(Instant.now())
                .eventType("FORECAST_REGENERATED")
                .build());
    }

    /**
     * Adds a metric to the forecast
     *
     * @param metric the metric to add
     */
    public void addMetric(ForecastMetric metric) {
        if (this.metrics == null) {
            this.metrics = new ArrayList<>();
        }
        this.metrics.add(metric);
        recalculateTotal();
    }

    /**
     * Updates the actual amount and calculates variance
     *
     * @param actualAmount the actual amount
     */
    public void updateActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
        calculateVariance();
    }

    /**
     * Archives the forecast
     */
    public void archive() {
        if (this.status == ForecastStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Cannot archive forecasts pending approval");
        }
        this.status = ForecastStatus.ARCHIVED;
    }

    /**
     * Updates the confidence level
     *
     * @param confidenceLevel the confidence level (0-100)
     * @throws IllegalArgumentException if confidence level is invalid
     */
    public void updateConfidenceLevel(Integer confidenceLevel) {
        if (confidenceLevel < 0 || confidenceLevel > 100) {
            throw new IllegalArgumentException("Confidence level must be between 0 and 100");
        }
        this.confidenceLevel = confidenceLevel;
    }

    /**
     * Sets scenario for the forecast
     *
     * @param scenario the scenario identifier
     */
    public void setScenario(String scenario) {
        this.scenario = scenario;
        addDomainEvent(ForecastGeneratedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .forecastType(this.forecastType.name())
                .scenario(scenario)
                .timestamp(Instant.now())
                .eventType("FORECAST_SCENARIO_UPDATED")
                .build());
    }

    /**
     * Validates forecast before submission
     */
    private void validateForecast() {
        if (this.name == null || this.name.isBlank()) {
            throw new IllegalArgumentException("Forecast name is required");
        }
        if (this.startDate == null) {
            throw new IllegalArgumentException("Forecast start date is required");
        }
        if (this.endDate == null) {
            throw new IllegalArgumentException("Forecast end date is required");
        }
        if (this.endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("Forecast end date must be after start date");
        }
        if (this.forecastType == null) {
            throw new IllegalArgumentException("Forecast type is required");
        }
        if (this.forecastHorizon == null) {
            throw new IllegalArgumentException("Forecast horizon is required");
        }
        if (this.currency == null || this.currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
    }

    /**
     * Recalculates total forecast amount from metrics
     */
    public void recalculateTotal() {
        if (this.metrics != null && !this.metrics.isEmpty()) {
            this.totalForecastAmount = this.metrics.stream()
                    .map(ForecastMetric::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    /**
     * Calculates variance between forecast and actual amounts
     */
    private void calculateVariance() {
        if (this.totalForecastAmount != null && this.actualAmount != null) {
            this.varianceAmount = this.actualAmount.subtract(this.totalForecastAmount);
            if (this.totalForecastAmount.compareTo(BigDecimal.ZERO) != 0) {
                this.variancePercentage = this.varianceAmount
                        .divide(this.totalForecastAmount, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
            } else {
                this.variancePercentage = BigDecimal.ZERO;
            }
        }
    }

    /**
     * Adds a domain event
     *
     * @param event the domain event
     */
    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    /**
     * Clears all domain events
     */
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Generates a unique forecast ID
     *
     * @return the forecast ID
     */
    private static String generateForecastId() {
        return "FC-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Checks if forecast is in editable state
     *
     * @return true if editable
     */
    public boolean isEditable() {
        return this.status == ForecastStatus.DRAFT || this.status == ForecastStatus.REJECTED;
    }

    /**
     * Checks if forecast can be submitted for approval
     *
     * @return true if can submit
     */
    public boolean canSubmit() {
        return this.status == ForecastStatus.DRAFT;
    }
}
